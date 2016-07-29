package com.mrsnottypants.factory;

import java.util.*;

/**
 * Created by Eric on 7/28/2016.
 */
public class FactoryFloor {

    // where to find the block : block id -> floor position
    private final Map<Integer, Integer> blockPosition;

    // list of blocks on each floor position
    private final Map<Integer, Stack<Integer>> blocksOnPosition;

    /**
     * Return a new instance of a factory floor, with specified count of spots for blocks.
     * Blocks are assigned a position equal to their starting position.
     * @param positionCount floor starts with this many positions. each position starts with a block.
     * @return factory floor
     */
    public static FactoryFloor newInstance(int positionCount) {

        if (positionCount < 0) { throw new IllegalArgumentException("positionCount must be positive"); }
        return new FactoryFloor(positionCount);
    }

    /**
     * Construct a new instance of a factory floor, with specified count of spots for blocks.
     * Blocks are assigned a position equal to their starting position.
     * @param positionCount floor starts with this many positions. each position starts with a block.
     */
    private FactoryFloor(int positionCount) {

        blockPosition = new HashMap<>();
        blocksOnPosition = new HashMap<>();

        // put blocks at their initial floor position
        // note: blocks are assign an ID equal to their starting position
        for (int position = 0 ; position < positionCount ; position++) {
            blocksOnPosition.put(position, new Stack<>());
            putBlock(position, position);
        }
    }

    /**
     * Putting a block at a position requires 2 steps:
     * Update block's entry in blockPosition.
     * Push block onto stack in blocksOnPosition
     * @param position where to put block
     * @param block block to put
     */
    private void putBlock(int position, int block) {

        // set position
        blockPosition.put(block, position);

        // put block in stack at position
        blocksOnPosition.get(position).push(block);
    }

    /**
     * Removes and returns the top block at the specified position.
     * Removes the block's entry from blockPositions - it is currently in-transition.
     * @param position take top block from this position
     * @return top block
     */
    private int takeBlock(int position) {

        // pop block off of position
        int block = blocksOnPosition.get(position).pop();

        // remove block from block position map
        blockPosition.remove(block);

        // return block that was on position
        return block;
    }

    /**
     * Throws a no-such-element exception if the position or block is unknown
     * @param id position or block
     */
    private void confirmLegal(int id) {
        if ((id < 0) || (id >= blocksOnPosition.size() )) {
            throw new NoSuchElementException(String.format("no element at %d", id));
        }
    }

    /**
     * Returns a read-only list of blocks at the given position.
     * Ordering is from bottom block to top block
     * @param position position of blocks
     * @return list of blocks
     */
    public List<Integer> getBlocksAt(int position) {

        // sanity check position
        confirmLegal(position);

        // return read-only list of blocks
        return Collections.unmodifiableList(blocksOnPosition.get(position));
    }

    /**
     * Returns the floor position of a given block
     * @param block block
     * @return floor position
     */
    public int getBlockPosition(int block) {

        // sanity check block id
        confirmLegal(block);

        // return block position
        return blockPosition.get(block);
    }

    /**
     * Move a block onto another block.
     * Any blocks above the from and to blocks are first moved back to their starting position.
     * @param blockFrom block to move
     * @param blockTo block to move onto
     * @return true if one or more blocks are moved
     */
    public boolean moveOnto(int blockFrom, int blockTo) {
        return move(blockFrom, blockTo, true, true);
    }

    /**
     * Move a block over another block.
     * Any blocks above the from block are first moved back to their starting position.
     * @param blockFrom block to move
     * @param blockTo block to move over
     * @return true if one or more blocks are moved
     */
    public boolean moveOver(int blockFrom, int blockTo) {
        return move(blockFrom, blockTo, true, false);
    }

    /**
     * Move a block, and the blocks above it, onto another block.
     * Any blocks above the to block are first moved back to their starting position.
     * @param blockFrom block to move
     * @param blockTo block to move over
     * @return true if one or more blocks are moved
     */
    public boolean pileOnto(int blockFrom, int blockTo) {
        return move(blockFrom, blockTo, false, true);
    }

    /**
     * Move a block, and the blocks above it, over another block.
     * @param blockFrom block to move
     * @param blockTo block to move over
     * @return true if one or more blocks are moved
     */
    public boolean pileOver(int blockFrom, int blockTo) {
        return move(blockFrom, blockTo, false, false);
    }

    /**
     * Move a block, which may result in other blocks also being moved.
     * Nothing happens if blockFrom is the same as blockTo, or the 2 blocks already occupy the same position.
     * @param blockFrom block to move
     * @param blockTo block is moved to the position that contains this block
     * @param resetPositionFrom if true, all blocks above the from-block are returned to their starting positions
     * @param resetPositionTo if true, all blocks above the to-block are returned to their starting positions
     * @return true if one or more blocks are moved
     */
    private boolean move(int blockFrom, int blockTo, boolean resetPositionFrom, boolean resetPositionTo) {

        // sanity check - same block
        if (blockFrom == blockTo) { return false; }

        // find block to move - also confirms legal block id
        int positionFrom = getBlockPosition(blockFrom);

        // find block to move on top of - also confirms legal block id
        int positionTo = getBlockPosition(blockTo);

        // sanity check - same position
        if (positionFrom == positionTo) { return false; }

        // optional: clear blocks from position until our block is on top
        // if we don't do this, we also move all blocks above the from block
        if (resetPositionFrom) { resetPosition(positionFrom, blockFrom); }

        // optional: clear blocks from position until our block is on top
        if (resetPositionTo) { resetPosition(positionTo, blockTo); }

        // move block(s)
        movePosition(positionFrom, positionTo, blockFrom);
        return true;
    }

    /**
     * Returns blocks at this position to their start position.
     * Stops once it finds the specified block (which is not moved).
     * @param position to reset
     * @param block all blocks above this block are reset
     */
    private void resetPosition(int position, int block) {

        // while position has blocks...
        Stack<Integer> blocks = blocksOnPosition.get(position);
        while(blocks.size() > 0) {

            // if top block is our block - done
            int topBlock = blocks.peek();
            if (topBlock == block) { break; }

            // otherwise - return block to its original position
            movePosition(position, topBlock, topBlock);
        }
    }

    /**
     * Moves blocks from one position to another.
     * @param positionFrom blocks are moved from this floor position
     * @param positionTo blocks are moved to this floor position
     * @param block all blocks down to this block inclusive are moved
     */
    private void movePosition(int positionFrom, int positionTo, int block) {

        // build up pile of blocks to be moved
        Stack<Integer> pile = new Stack<>();
        while(blocksOnPosition.get(positionFrom).size() > 0) {

            // move block onto pile
            pile.push(takeBlock(positionFrom));

            // stop once we reach the desired block
            if (pile.peek() == block) { break; }
        }

        // put pile onto position
        while (pile.size() > 0) {
            putBlock(positionTo, pile.pop());
        }
    }

    /**
     * Human readable floor positions
     * @return floor positions
     */
    @Override
    public String toString() {
        return output(" | ");
    }

    /**
     * Supports both the standard toString and exercise's desired output
     * @param divider Position outputs are divided by this string
     * @return output
     */
    public String output(String divider) {

        // for each floor position..
        StringBuilder builder = new StringBuilder();
        for (int j = 0; j < blocksOnPosition.size() ; j++) {

            // after 1st entry, add newlines
            if (j > 0) { builder.append(divider); }

            // add floor position
            builder.append(String.format("%d:", j));

            // add each block at that floor position
            for (int block : blocksOnPosition.get(j)) {
                builder.append(String.format(" %d", block));
            }
        }
        return builder.toString();
    }
}
