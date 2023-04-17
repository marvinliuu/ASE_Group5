package com.example.testdisasterevent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.testdisasterevent.algorithms.PasswordEncryption;
import com.example.testdisasterevent.algorithms.ResearchAllocation;

import org.junit.Test;
import org.junit.runner.RunWith;


public class ResearchAllocationTest {
    @Test
    public void testGetAllocationPolice() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        float[][] input = {{100.0f}, {4.0f}};
        // test police
        int modelType = 2;
        ResearchAllocation allocation = new ResearchAllocation();
        int result = allocation.getAllocation(context, input, modelType);
        // Verify that the output value matches the expected result
        assertEquals(24, result);
    }

    @Test
    public void testGetAllocationAmbulance() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        float[][] input = {{100.0f}, {4.0f}};
        // test police
        int modelType = 1;
        ResearchAllocation allocation = new ResearchAllocation();
        int result = allocation.getAllocation(context, input, modelType);
        // Verify that the output value matches the expected result
        assertEquals(5, result);
    }

    @Test
    public void testGetAllocationTrunk() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        float[][] input = {{100.0f}, {10.0f}};
        // test police
        int modelType = 3;
        ResearchAllocation allocation = new ResearchAllocation();
        int result = allocation.getAllocation(context, input, modelType);
        // Verify that the output value matches the expected result
        assertEquals(0, result);
    }

    @Test
    public void testGetAllocationBus() {
        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        float[][] input = {{100.0f}, {10.0f}};
        // test police
        int modelType = 4;
        ResearchAllocation allocation = new ResearchAllocation();
        int result = allocation.getAllocation(context, input, modelType);
        // Verify that the output value matches the expected result
        assertEquals(1, result);
    }
}
