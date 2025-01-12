package com.blogspot.groglogs.maicar;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

//runs on android device
@RunWith(AndroidJUnit4.class)
public class AppInstrumentedTest {
    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.blogspot.groglogs.maicar", appContext.getPackageName());
    }
}