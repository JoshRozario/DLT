<template>
  <div class="hello">
  <section class="top">
      <div class="menu">
        <div class="menu-item menu-item-title menu-text-top">
        THE GREAT DLT APP
        </div>
        <div class="menu-item menu-center menu-text-top">
          <a @click="deleteTransaction(node, id)">Delete Transaction</a>
          <input class="sub" v-model="node" placeholder="nID" >
          <input class="sub" v-model="id" placeholder="tID" >
        </div>
        <div class="menu-item menu-text-top">
          <a @click="unSubscribe(nodeA, nodeB)">Unsubscribe </a>
          <input class="sub" v-model="nodeA" placeholder="ID">
          <input class="sub" v-model="nodeB" placeholder="ID">  
        </div>
      </div>
    </section>

    <section class="middle">
      <div class="menu">
        <div @click="showLeft()" class="menu-item">
          {{nodeResponse}}
        </div>
        <div class="menu-item">
          {{postResponse}}
          <div id="app">
            <select @change="showTransactions($event)">
                <option value="">Please select one</option>
                <option v-for="item in response" :value="item" v-bind:key="item">{{item}}</option>
            </select>
            <br/>
              Transactions:
            <br/>
              <p class="list" v-for= "tran in transactions" :value = "tran" v-bind:key="tran">ID: {{tran.transactionID}} Amount: {{tran.amount}}</p>
          </div>

        </div>
        <div class="menu-item">
          <div id="app">
            <select @change="showSubscriptions($event)">
                  <option value="">Please select one</option>
                  <option v-for="it in response" :value="it" v-bind:key="it">{{it}}</option>
            </select>
          </div>
          Subscriptions:
          <br/>
              <p class="list" v-for= "subs in subscriptions" :value = "subs" v-bind:key="subs">{{subs}}</p>

          <br/>

          {{subResponse}}
          <br/>
          List of current Nodes:
          <br/>
          {{response.toString()}}
        </div>
      </div>
    </section>


    <section class="bottom">
      <div class="menu">
        <div  class="menu-item menu-text">
          <a @click="createNode(message)">Create Node </a>
          <input class="sub" v-model="message" placeholder="ID" >
        </div>
        <div class="menu-item menu-text menu-center">
          <a @click="postTransaction(node, id, amount)">Post Transaction</a>
          <input class="sub" v-model="node" placeholder="nID" >
          <input class="sub" v-model="id" placeholder="tID" >
          <input class="sub" v-model="amount" placeholder="amt">
        </div>
        <div class="menu-item menu-text">
          <a @click="subscribe(nodeA, nodeB)">Subscribe </a>
          <input class="sub" v-model="nodeA" placeholder="ID">
          <input class="sub" v-model="nodeB" placeholder="ID">
        </div>
      </div>
    </section>
  </div>
</template>

<script>
import axios from 'axios'
export default {
  name: 'Home',
  props: {
    msg: String
  },
  data(){
    return {
      subResponse: '',
      postResponse: '',
      nodeResponse: '',
      message: '',
      nodeA: '',
      nodeB: '',
      node: '',
      id: '',
      amount: '',
      response: '',
      transactions:'',
      subscriptions:''
    }
  },
  methods: {
    async createNode(message){
      let config = {
        headers: {
          'Accept' : 'application/json'
        }
      }
    const response = await axios.post(`http://localhost:8080/createnode/${message}`, config)
    this.nodeResponse = response.data
    this.showNodes()
    },
    async showNodes(){
      let config = {
        headers: {
          'Accept' : 'application/json'
        }
      }
    const response = await axios.get('http://localhost:8080/getallnodes', config)
    this.response = (response.data)
    },

    async subscribe(nodeA, nodeB){
    const response = await axios.post(`http://localhost:8080/subscribe/${nodeA}/${nodeB}`)
    this.subResponse = (response.data)
    },
    async unSubscribe(nodeA, nodeB){
    const response = await axios.post(`http://localhost:8080/unsubscribe/${nodeA}/${nodeB}`)
    this.subResponse = response.data
    },
      async postTransaction(node, id, amount){
      let config = {
        headers: {
          'Accept' : 'application/json'
        }
      }
    const response = await axios.post(`http://localhost:8080/posttransaction/${node}/${id}/${amount}`, config)
    this.postResponse = response.data
    },
    async deleteTransaction(node, id){
      let config = {
        headers: {
          'Accept' : 'application/json'
        }
      }
    const response = await axios.post(`http://localhost:8080/deletetransaction/${node}/${id}`, config)
    console.log(response.data)
    this.postResponse = response.data
    },
    async showTransactions(event){
      console.log(event.target.value)
      let config = {
        headers: {
          'Accept' : 'application/json'
        }
      }
    const response = await axios.get(`http://localhost:8080/gettransactions/${event.target.value}`, config)
    console.log(response.data)
    this.transactions = response.data
    },
    async showSubscriptions(event){
      console.log(event.target.value)
      let config = {
        headers: {
          'Accept' : 'application/json'
        }
      }
    const response = await axios.get(`http://localhost:8080/getsubscriptions/${event.target.value}`, config)
    console.log(response.data)
    this.subscriptions = response.data
    if(response.data.toString().length < 1 ){
      this.subscriptions = ["No subscriptions"]
    }
    }
  },
   beforeMount(){
    this.showNodes()
 },
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>


  .menu {
    display: flex;
  }
  .menu-item {
    flex: 1;
    text-align: center;
  }
  .menu-text {
    font-size: 20px;
    color: #818181;
    padding: 10px;
    cursor: pointer;
    border-top: 2px solid #c148ff;
    width: fit-content;
  }
  .menu-text:hover{
    border-top: 4px solid #c278ff;
  }

  .menu-text-top {
    font-size: 20px;
    color: #818181;
    padding: 10px;
    cursor: pointer;
    border-bottom: 2px solid #c148ff;
    width: fit-content;
  }

  .menu-text-top:hover{
    border-bottom: 4px solid #c278ff;
  }

   .menu-item-title{
    border-bottom: 2px solid #c278ff;
    border-right: 2px solid #c278ff;
    border-radius: 0px 0px 20px 0px; 
    color: #fff;
    font-weight: bold;
  }

  .bottom{
    width: 100%;
    position: fixed;
    top: 90vh;
  }

  .middle{
    width: 100%;
    position: fixed;
    top:50vh
  }

  .sub{
    width: 20px;
    padding: 5px;
    margin: 5px
  }
  .list{
    font-size:15px;
    padding: 1px;
    margin: 5px;
  }

</style>
