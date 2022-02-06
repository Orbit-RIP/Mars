/**
 * Handles reading and accessing {@link rip.orbit.mars.arena.Arena}s
 * and {@link rip.orbit.mars.arena.ArenaSchematic}s.
 *
 * An ArenaSchematic is a template for creating Arenas. ArenaSchematics correlate
 * with a .schematic file which can be pasted, and Arenas correlate with a specific
 * paste of said schematic file. Common attributes such as min/max player count
 * are stored in ArenaSchematics, as they are shared between all instances of an Arena,
 * where as specific things such as spawn points (which change per instance) are stored
 * in an Arena.
 */
package rip.orbit.mars.arena;