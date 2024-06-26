package rip.orbit.mars.persist;

import cc.fyre.proton.Proton;
import cc.fyre.proton.util.UUIDUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;
import rip.orbit.mars.Mars;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PersistMap<T> {

	protected Map<UUID, T> wrappedMap = new ConcurrentHashMap<>();

	private boolean useMongo;
	
	@Setter private String keyPrefix;
	@Setter private String mongoName;

	public void loadFromRedis() {
		Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
			Map<String, String> results = redis.hgetAll(keyPrefix);

			for (Map.Entry<String, String> resultEntry : results.entrySet()) {
				T object = getJavaObjectSafe(resultEntry.getKey(), resultEntry.getValue());

				if (object != null) {
					wrappedMap.put(UUID.fromString(resultEntry.getKey()), object);
				}
			}

			return (null);
		});
	}

	protected void wipeValues() {
		wrappedMap.clear();

		Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
			redis.del(keyPrefix);
			return (null);
		});
	}

	protected void updateValueSync(UUID key, T value) {
		wrappedMap.put(key, value);

		Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {

			redis.hset(keyPrefix, key.toString(), getRedisValue(getValue(key)));

			boolean succeeded = false;
			int tries = 0;
			while (useMongo && !succeeded && tries++ < 5) {
				try {
					DBCollection playersCollection = Mars.getInstance().getDB().getCollection("Players");
					BasicDBObject player = new BasicDBObject("_id", key.toString().replace("-", ""));

					BasicDBObject toSet = new BasicDBObject(mongoName, getMongoValue(getValue(key)));
					toSet.put("lastUsername", UUIDUtils.name(key));

					playersCollection.update(player, new BasicDBObject("$set", toSet), true, false);
					succeeded = true;
				} catch (Exception e) {
					succeeded = false;
				}
			}

			if (useMongo && !succeeded) {
				throw new RuntimeException("Mongo insert failed after five retries! User: " + key + ".");
			}
			return (null);

		});
	}

	protected void updateValueAsync(UUID key, T value) {
		wrappedMap.put(key, value);

		new BukkitRunnable() {

			@Override
			public void run() {
				Proton.getInstance().getIRedisCommand().runRedisCommand(redis -> {
					redis.hset(keyPrefix, key.toString(), getRedisValue(getValue(key)));

					boolean succeeded = false;
					int tries = 0;
					while (useMongo && !succeeded && tries++ < 5) {
						try {
							DBCollection playersCollection = Mars.getInstance().getDB().getCollection("Players");
							BasicDBObject player = new BasicDBObject("_id", key.toString().replace("-", ""));

							BasicDBObject toSet = new BasicDBObject(mongoName, getMongoValue(getValue(key)));
							toSet.put("lastUsername", UUIDUtils.name(key));

							playersCollection.update(player, new BasicDBObject("$set", toSet), true, false);
							succeeded = true;
						} catch (Exception e) {
							succeeded = false;
						}
					}

					if (useMongo && !succeeded) {
						throw new RuntimeException("Mongo update failed after five retries! User: " + key + ". " + value);
					}
					return (null);
				});
			}

		}.runTaskAsynchronously(Mars.getInstance());
	}

	protected T getValue(UUID key) {
		return (wrappedMap.get(key));
	}

	protected boolean contains(UUID key) {
		return (wrappedMap.containsKey(key));
	}

	public abstract String getRedisValue(T t);

	public abstract Object getMongoValue(T t);

	public T getJavaObjectSafe(String key, String redisValue) {
		try {
			return (getJavaObject(redisValue));
		} catch (Exception e) {
			System.out.println("Error parsing Redis result.");
			System.out.println(" - Prefix: " + keyPrefix);
			System.out.println(" - Key: " + key);
			System.out.println(" - Value: " + redisValue);
			return (null);
		}
	}

	public abstract T getJavaObject(String str);

}