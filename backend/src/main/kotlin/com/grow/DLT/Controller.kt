package com.grow.DLT

import com.google.gson.Gson
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.RestController




@CrossOrigin(origins = arrayOf("*"))
@RestController
class RestController {

    val ledger = Ledger();
    var gson = Gson()


    //get requests
    @GetMapping(value = ["/help"])
    fun test(): String {
        return "I GET A RETURN WOOOOOO!"
    }

    @GetMapping(value = ["/getnode/{input}"])
    fun getNode(@PathVariable input: String): String? {
        var node = ledger.getNode(input);
        println(node)
        return if (node != null) {
            gson.toJson(node)
        } else {
            null
        }
    }

    @GetMapping(value = ["/getallnodes"])
    fun getAllNodes(): String? {
        var listNames = ledger.getAllNodeNames()
        return gson.toJson(listNames)
    }

    @GetMapping(value = ["/gettransactions/{node}"])
    fun getTransactions(@PathVariable node:String): String? {
        return gson.toJson(ledger.getTransactions(node))
    }

    @GetMapping(value = ["/getsubscriptions/{node}"])
    fun getSubscriptions(@PathVariable node:String): String? {
        return ledger.getSubscriptions(node);
    }

    //post requests
    @PostMapping(value = ["/createnode/{input}"])
    fun createNode(@PathVariable input: String): String {
        if (ledger.createNode(input)) {
            return ("created node with name '$input'")
        } else {
            return ("something went wrong");
        }
    }

    @PostMapping(value = ["/posttransaction/{name}/{id}/{amount}"])
    fun postTransaction(@PathVariable name: String, @PathVariable id: Long, @PathVariable amount: Double): String {
        return if (ledger.postTransaction(name, id, amount)) {
            "created transaction in $name with id $id and amount of $amount"
        } else {
            "something went wrong"
        }
    }

    @PostMapping(value = ["/deletetransaction/{name}/{id}"])
    fun deleteTransaction(@PathVariable name: String, @PathVariable id: Long): String {
        return if(ledger.deleteTransaction(name, id)){
            "Deleted transaction in $name with id $id"
        }else{
            "something went wrong"
        }
    }

    @PostMapping(value = ["/subscribe/{node1}/{node2}"])
    fun subscribe(@PathVariable node1: String, @PathVariable node2: String): String {
        if (ledger.subscribe(node1,node2)){
            return "subscribed node ${node1} to ${node2}"
        }else {
            return "something went wrong"
        }
    }

    @PostMapping(value = ["/unsubscribe/{node1}/{node2}"])
    fun unsubscribe(@PathVariable node1: String, @PathVariable node2: String): String {
        if (ledger.unsubscribe(node1,node2)){
            return "unsubscribed node ${node1} from ${node2}"
        }else {
            return "something went wrong"
        }
    }


}
data class NodeData(val name: String, var history: MutableList<Transaction> = mutableListOf())
