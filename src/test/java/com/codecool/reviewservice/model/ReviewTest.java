package com.codecool.reviewservice.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReviewTest {

    Review review = new Review(2, "So me Thing", "This is comment", 4, "Jack");
    Review reviewWithKey = new Review(3, "What ELse", "wtf", 2, "7abc9587-2f0f-4059-87e8-349edaf7f247", "Status",
            "Thomas");

    @Test
    public void getStatus() throws Exception {
        assertEquals("PENDING", review.getStatus());
    }

    @Test
    public void setStatusApproved() throws Exception {
        review.setStatusApproved();
        assertEquals("APPROVED", review.getStatus());
    }

    @Test
    public void setStatusDenied() throws Exception {
        review.setStatusDenied();
        assertEquals("DENIED", review.getStatus());
    }

    @Test
    public void testHandleProductName(){
     assertEquals("SOMETHING", review.getProductName());
    }
//    Test model with reviewKey
    @Test
    public void setStatusApprovedWithKey() throws Exception {
        reviewWithKey.setStatusApproved();
        assertEquals("APPROVED", reviewWithKey.getStatus());
    }

    @Test
    public void setStatusDeniedWithKey() throws Exception {
        reviewWithKey.setStatusDenied();
        assertEquals("DENIED", reviewWithKey.getStatus());
    }

    @Test
    public void testHandleProductNameWithKey(){
        assertEquals("WHATELSE", reviewWithKey.getProductName());
    }
}