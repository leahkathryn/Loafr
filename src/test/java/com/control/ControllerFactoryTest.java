package com.control;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ControllerFactoryTest
{
    @BeforeAll
    static void beforeAll()
    {

    }

    // test case naming convention: MethodName_StateUnderTest_ExpectedBehavior
    @Test
    void GetController_ParseOutputFileLocationFromInput_Success() {
        System.out.println("**--- Test method1 executed ---**");
    }
}
