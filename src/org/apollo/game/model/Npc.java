package org.apollo.game.model;

import org.apollo.game.model.def.NpcDefinition;
import org.apollo.game.sync.block.SynchronizationBlock;

/**
 * An {@link Npc} is a {@link Mob} that is not being controlled by a player.
 * 
 * @author Major
 */
@SuppressWarnings("serial")
public final class Npc extends Mob {

	/**
	 * The positions representing the bounds (i.e. walking limits) of this npc.
	 */
	private Position[] boundary;

	/**
	 * Creates a new npc with the specified id and {@link Position}.
	 * 
	 * @param id The id.
	 * @param position The position.
	 */
	public Npc(int id, Position position) {
		this(NpcDefinition.lookup(id), position);
	}

	/**
	 * Creates a new npc with the specified {@link NpcDefinition} and {@link Position}.
	 * 
	 * @param definition The definition.
	 * @param position The position.
	 */
	public Npc(NpcDefinition definition, Position position) {
		super(position);
		this.definition = definition;
	}

	/**
	 * Gets the boundary of this npc.
	 * 
	 * @return The boundary.
	 */
	public Position[] getBoundary() {
		return boundary;
	}

	@Override
	public EntityType getEntityType() {
		return EntityType.NPC;
	}

	/**
	 * Gets the id of this npc. Shorthand for {@link #getDefinition().getId()}.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return definition.getId();
	}

	/**
	 * Indicates whether or not this npc is bound to a specific set of coordinates.
	 * 
	 * @return {@code true} if the npc is bound, otherwise {@code false}.
	 */
	public boolean isBound() {
		return boundary == null;
	}

	/**
	 * Sets the boundary of this npc.
	 * 
	 * @param boundary The boundary.
	 */
	public void setBoundary(Position[] boundary) {
		if (boundary.length != 4) {
			throw new IllegalArgumentException("Boundary count must be 4.");
		}
		this.boundary = boundary;
	}

	@Override
	public String toString() {
		return "[" + Npc.class.getName() + ": id=" + definition.getId() + ", name=" + definition.getName() + "]";
	}

	/**
	 * Transforms this npc into the npc with the specified id.
	 * 
	 * @param id The id.
	 */
	public void transform(int id) {
		if (id < 0 || id >= NpcDefinition.count()) {
			throw new IllegalArgumentException("Id to transform to is out of bounds.");
		}
		definition = NpcDefinition.lookup(id);
		blockSet.add(SynchronizationBlock.createTransformBlock(id));
	}

}