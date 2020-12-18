package com.grow.DLT

import java.util.*


class Transaction(
        var transactionID: Long,
        var amount: Double,
        var timeStamp: Calendar = Calendar.getInstance()
)


//class State(
//        var stateID: Long,
//        var previousId: Long,
//        var transaction: Transaction?,
//)

class Node(
        val name: String,
        var history: MutableList<Transaction> = mutableListOf(),
){
    fun getCurrState(): Transaction {
        return history.first()
    }

    fun deleteTransaction(id: Long): Boolean {
        val transactions = history.map { it.transactionID }
        if (transactions.contains(id)){
            var transaction = history.filter { it.transactionID == id }
            var tobeRemoved = transaction[0]
            history.remove(tobeRemoved)
            return true
        }else{
            return false
        }
    }



    fun newTransaction(amount : Double, ID: Long): Boolean {
        return if (!iDexistsIn(ID)){
            history.add(0,Transaction(ID,amount))
            true
        }else{
            false
        }
    }
    private fun iDexistsIn(ID:Long): Boolean{
        val IDs =  history.map { it.transactionID }
        return IDs.contains(ID)
    }








}

class Ledger(
        var nodes: MutableList<Node> = mutableListOf(),
        var subscriptions: MutableMap<String, String> = mutableMapOf()
) {

    fun postTransaction(nodeName: String, transactionID: Long,amount: Double): Boolean {
        val names = nodes.map { it.name }

        val ids : MutableList<Long>? = getTransactionIDs(nodeName) as MutableList<Long>?
        nodes.forEach(){
            if (ids != null) {
                getTransactionIDs(it.name)?.let { it1 -> ids.addAll(it1) }
            }
        }
        if (ids != null && ids.contains(transactionID)){
            return false
        }

        return if (names.contains(nodeName)){
            val postNode = nodes.filter { it.name == nodeName }
            return postNode[0].newTransaction(amount, transactionID)
        }else{
            false
        }
    }

    fun deleteTransaction(nodeName:String, id: Long ): Boolean {
        val names = nodes.map { it.name }

        if (names.contains(nodeName)){
            val nodetoDelete = nodes.filter { it.name == nodeName}
            val node = nodetoDelete[0]
            node.deleteTransaction(id);
            return true
        }else{
            return false
        }


    }

    fun getNode(nodeName: String): Node?{
        val list =  nodes.filter { it.name == nodeName }
        return if(list.isNotEmpty()){
            list[0]
        }else {
            null
        }
    }

    fun getAllNodeNames(): List<String>{
        return nodes.map { it.name }
    }

    fun createNode(nodeName: String): Boolean {
        return if (getNode(nodeName) == null) {
            nodes.add(Node(nodeName))
            true
        } else {
            false
        }
    }

    fun subscribe(node1: String, node2: String): Boolean {
        if(getNode(node1) != null && getNode(node2) != null && !subscriptions.contains(node1)){
            subscriptions[node1] = node2
            return true
        }else{
            println("both or one of those nodes didn't exist")
            return false
        }
    }



    fun unsubscribe(node1: String, node2: String): Boolean {
        if(subscriptions.contains(node1)){
            subscriptions.remove(node1)
            return true
        }else{
            println("This subscription does not exist")
            return false
        }
    }

    fun getSubscriptions(node1: String): String? {
        if(subscriptions.contains(node1)){
            return subscriptions[node1]
        }else{
            return null
        }
    }


    fun getTransactionIDs(nodeName: String): List<Long>? {
        var node =  getNode(nodeName)
        if (node != null) {
            if(subscriptions.contains(node.name)){
                var nodeTwo = subscriptions[node.name]
                var listTwo = getNode(nodeTwo!!)
                return (listTwo!!.history.map { it.transactionID }) + (node.history.map { it.transactionID })
            }else{
                return node.history.map { it.transactionID }
            }
        }
        return null
    }

    fun getTransactions(nodeName: String): List<Transaction>? {
        var node =  getNode(nodeName)
        if (node != null) {
            if(subscriptions.contains(node.name)){
                var nodeTwo = subscriptions[node.name]
                var listTwo = getNode(nodeTwo!!)
                return listTwo!!.history + node.history
            }else{
                return node.history
            }
        }
        return null
    }

}

