package com.mrsnottypants.factory;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Eric on 7/28/2016.
 */
public class FactoryFloorTest {

    // assert blocks at given position are as expected
    //
    private void assertBlocks(FactoryFloor floor, int position, List<Integer> expected) {
        List<Integer> blocks = floor.getBlocksAt(position);
        assertEquals(expected.size(), blocks.size());
        for (int j = 0 ; j < expected.size() ; j++) {
            assertEquals(expected.get(j), blocks.get(j));
        }
    }

    @Test
    public void testSetup() {

        // create factory floor
        FactoryFloor floor = FactoryFloor.newInstance(8);

        for (int position = 0 ; position < 8 ; position++) {

            // confirm each position holds one block, and its id matches the position
            assertBlocks(floor, position, Arrays.asList(position));

            // confirm each block's position matches
            assertEquals(position, floor.getBlockPosition(position));
        }
    }

    @Test
    public void testSetupIllegal() {
        assertTrue(Exceptions.isExpected(FactoryFloor::newInstance, -1, IllegalArgumentException.class));
    }

    @Test
    public void testGetBlocksAtNoSuchPosition() {
        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(Exceptions.isExpected(floor::getBlocksAt, -1, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::getBlocksAt, 4, NoSuchElementException.class));
    }

    @Test
    public void testGetBlockPositionNoSuchBlock() {
        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(Exceptions.isExpected(floor::getBlockPosition, -1, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::getBlockPosition, 4, NoSuchElementException.class));
    }

    @Test
    public void testMoveOntoBasic() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testMoveOntoResetFrom() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOnto(2, 3));

        // expect - 0: 0 | 1: 1 | 2: | 3: 3 2
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList(1));
        assertBlocks(floor, 2, Arrays.asList());
        assertBlocks(floor, 3, Arrays.asList(3, 2));
    }

    @Test
    public void testMoveOntoResetTo() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOnto(3, 2));

        // expect - 0: 0 | 1: 1 | 2: 2 3 | 3:
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList(1));
        assertBlocks(floor, 2, Arrays.asList(2, 3));
        assertBlocks(floor, 3, Arrays.asList());
    }

    @Test
    public void testMoveOntoSameBlock() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.moveOnto(2, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testMoveOntoSamePosition() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.moveOnto(2, 1));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testMoveOntoNoSuchElement() {
        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(Exceptions.isExpected(floor::moveOnto, -1, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::moveOnto, 4, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::moveOnto, 0, -1, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::moveOnto, 0, 4, NoSuchElementException.class));
    }

    @Test
    public void testMoveOverBasic() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOver(1, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testMoveOverResetFrom() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOver(2, 3));

        // expect - 0: 0 | 1: 1 | 2: | 3: 3 2
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList(1));
        assertBlocks(floor, 2, Arrays.asList());
        assertBlocks(floor, 3, Arrays.asList(3, 2));
    }

    @Test
    public void testMoveOverDontResetTo() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOver(3, 2));

        // expect - 0: 0 | 1: | 2: 2 1 3 | 3:
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1, 3));
        assertBlocks(floor, 3, Arrays.asList());
    }

    @Test
    public void testMoveOverSameBlock() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.moveOver(2, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testMoveOverSamePosition() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.moveOver(2, 1));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testMoveOverNoSuchElement() {
        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(Exceptions.isExpected(floor::moveOver, -1, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::moveOver, 4, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::moveOver, 0, -1, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::moveOver, 0, 4, NoSuchElementException.class));
    }

    @Test
    public void testPileOntoBasic() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.pileOnto(1, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testPileOntoDontResetFrom() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOver(3, 2));
        assertTrue(floor.pileOnto(1, 0));

        // expect - 0: 0 1 3 | 1: | 2: 2 | 3:
        assertBlocks(floor, 0, Arrays.asList(0, 1, 3));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2));
        assertBlocks(floor, 3, Arrays.asList());
    }

    @Test
    public void testPileOntoResetTo() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOnto(3, 0));
        assertTrue(floor.pileOnto(2, 0));

        // expect - 0: 0 2 1 | 1: | 2: | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0, 2, 1));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList());
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testPileOntoSameBlock() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.pileOnto(2, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testPileOntoSamePosition() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.pileOnto(2, 1));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testPileOntoNoSuchElement() {
        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(Exceptions.isExpected(floor::pileOnto, -1, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::pileOnto, 4, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::pileOnto, 0, -1, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::pileOnto, 0, 4, NoSuchElementException.class));
    }

    @Test
    public void testPileOverBasic() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.pileOver(1, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testPileOverDontResetFrom() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOver(3, 2));
        assertTrue(floor.pileOver(1, 0));

        // expect - 0: 0 1 3 | 1: | 2: 2 | 3:
        assertBlocks(floor, 0, Arrays.asList(0, 1, 3));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2));
        assertBlocks(floor, 3, Arrays.asList());
    }

    @Test
    public void testPileOverDontResetTo() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertTrue(floor.moveOnto(3, 0));
        assertTrue(floor.pileOver(2, 0));

        // expect - 0: 0 3 2 1 | 1: | 2: | 3:
        assertBlocks(floor, 0, Arrays.asList(0, 3, 2, 1));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList());
        assertBlocks(floor, 3, Arrays.asList());
    }

    @Test
    public void testPileOverSameBlock() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.pileOver(2, 2));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testPileOverSamePosition() {

        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(floor.moveOnto(1, 2));
        assertFalse(floor.pileOver(2, 1));

        // expect - 0: 0 | 1: | 2: 2 1 | 3: 3
        assertBlocks(floor, 0, Arrays.asList(0));
        assertBlocks(floor, 1, Arrays.asList());
        assertBlocks(floor, 2, Arrays.asList(2, 1));
        assertBlocks(floor, 3, Arrays.asList(3));
    }

    @Test
    public void testPileOverNoSuchElement() {
        FactoryFloor floor = FactoryFloor.newInstance(4);
        assertTrue(Exceptions.isExpected(floor::pileOver, -1, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::pileOver, 4, 0, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::pileOver, 0, -1, NoSuchElementException.class));
        assertTrue(Exceptions.isExpected(floor::pileOver, 0, 4, NoSuchElementException.class));
    }

    @Test
    public void testToString() {
        FactoryFloor floor = FactoryFloor.newInstance(2);

        // just confirm it doesn't break
        assertTrue(floor.toString().length() > 0);
    }

    @Test
    public void moveOntoExercise() {

        // create factory floor
        FactoryFloor floor = FactoryFloor.newInstance(8);

        floor.moveOnto(7, 1);
        floor.moveOnto(5, 1);
        floor.moveOnto(1, 6);
        floor.moveOnto(4, 3);
        floor.moveOnto(1, 4);
        floor.moveOnto(3, 1);
        floor.moveOnto(5, 2);
        floor.moveOnto(7, 5);
        floor.moveOnto(4, 5);

        System.out.println(floor.output("\n"));
    }
}