package com.grow.DLT

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test



class LedgerTest{
    var testLedger = Ledger()
    @Test
    fun nodeCreateTest() {
        testLedger.createNode("test")
        val node = testLedger.getNode("test")
        assertEquals("test", node!!.name)
    }

    @Test
    fun testGetEmptyNode(){
        assertEquals(null, testLedger.getNode("test"))
    }

    @Test
    fun testCreateExistingNode(){
        val shouldBeTrue = testLedger.createNode("test")
        val shouldBeFalse = testLedger.createNode("test")
        assertEquals(true, shouldBeTrue)
        assertEquals(false, shouldBeFalse)
    }

    @Test
    fun testTransactions(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        var node = testLedger.getNode("test");
        assertEquals(1, node!!.getCurrState().transactionID)
    }

    @Test
    fun testDuplicateTransactions(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        var node = testLedger.getNode("test");
        assertEquals(1, node!!.getCurrState().transactionID)
        testLedger.postTransaction("test", 1,24.00)
        assertEquals(false,testLedger.postTransaction("test", 1,24.00))
    }

    @Test
    fun testSeperateTransactions(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        var node = testLedger.getNode("test");
        assertEquals(1, node!!.getCurrState().transactionID)
        testLedger.postTransaction("test", 200,24.00)
        assertEquals(200, node!!.getCurrState().transactionID)
    }

    @Test
    fun subscribe(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        testLedger.createNode("wow")
        testLedger.postTransaction("wow", 2, 12.00)
        testLedger.postTransaction("wow", 4, 23.33)
        testLedger.subscribe("test", "wow")
        assertEquals("[4, 2, 1]", testLedger.getTransactionIDs("test").toString())
    }

    @Test
    fun preventSubscribeTransaction(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        testLedger.createNode("wow")
        testLedger.postTransaction("wow", 2, 12.00)
        testLedger.postTransaction("wow", 4, 23.33)
        testLedger.subscribe("test", "wow")
        assertEquals(false, testLedger.postTransaction("test",2,12.00))
    }

    @Test
    fun preventSameIDTransaction(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        assertEquals(false, testLedger.postTransaction("test", 1, 23.00))
    }

    @Test
    fun getTransactions(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        var x = testLedger.getTransactions("test")
        var trans = x?.get(0)
        if (trans != null) {
            assertEquals(1,trans.transactionID)
        }else{
            assertEquals("fail", "this will fail")
        }
    }

    @Test
    fun unsubscribe(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        testLedger.createNode("wow")
        testLedger.postTransaction("wow", 2, 12.00)
        testLedger.postTransaction("wow", 4, 23.33)
        testLedger.subscribe("test", "wow")
        assertEquals("[4, 2, 1]", testLedger.getTransactionIDs("test").toString())
        testLedger.unsubscribe("test", "wow")
        assertEquals("[1]", testLedger.getTransactionIDs("test").toString());
    }



    @Test
    fun deleteTransaction(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        assertEquals(true, testLedger.deleteTransaction("test",1))
    }

    @Test
    fun deleteTransactionSubscription(){
        testLedger.createNode("test")
        testLedger.postTransaction("test", 1,24.00)
        testLedger.createNode("wow")
        testLedger.postTransaction("wow", 2, 12.00)
        testLedger.postTransaction("wow", 4, 23.33)
        testLedger.subscribe("test", "wow")
        assertEquals("[4, 2, 1]", testLedger.getTransactionIDs("test").toString())
        testLedger.deleteTransaction("wow", 4);
        assertEquals("[2, 1]", testLedger.getTransactionIDs("test").toString())
    }

    @Test
    fun CodingChallengeSlide(){
        testLedger.createNode("A");
        testLedger.postTransaction("A", 100, 123.00);
        assertEquals("[100]", testLedger.getTransactionIDs("A").toString())
        testLedger.createNode("B");
        testLedger.subscribe("A", "B");
        testLedger.createNode("C")
        assertEquals(true, testLedger.postTransaction("B", 101, 123.00))
        assertEquals(false, testLedger.postTransaction("C", 101, 123.00))
        assertEquals(true, testLedger.postTransaction("C", 102, 123.00))
        assertEquals(true, testLedger.postTransaction("B", 103, 123.00))
        assertEquals(true, testLedger.postTransaction("C", 104, 123.00))
        assertEquals("[103, 101, 100]", testLedger.getTransactionIDs("A").toString())
        assertEquals("[103, 101]",testLedger.getTransactionIDs("B").toString())
        assertEquals( "[104, 102]", testLedger.getTransactionIDs("C").toString())
        assertEquals(true, testLedger.unsubscribe("A", "B"))
        assertEquals("[100]", testLedger.getTransactionIDs("A").toString())
    }









}
