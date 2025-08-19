package com.orangehrm.api;

import org.testng.annotations.Test;

public class CandidateApiTest {

    @Test
    public void testAddAndDeleteCandidate() {
        // Step 1: Login
        CandidateAPI.login("Admin", "admin123");

        // Step 2: Add candidate
        int id = CandidateAPI.addCandidate("Auto", "User", "auto.user@test.com");

        // Step 3: Delete candidate
        CandidateAPI.deleteCandidate(id);
    }
}
