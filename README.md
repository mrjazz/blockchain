[![Travis](https://img.shields.io/travis/mrjazz/blockchain.svg?style=flat-square)](build)

### Prototype of simple blockchain implementation

#### Transaction protocol

```

[START_TRANSACTION] client -> all

receiver <- sender [START_TRANSACTION]
receiver.doWork()
[VERIFY_WORK] receiver -> all

receiver <- sender [VERIFY_WORK]
receiver.validTransaction(transaction)
receiver -> sender [VERIFIED_WORK]

after confirm verification by 51% clients
miner.commitTransaction(transaction)
[FINISH_TRANSACTION] miner -> all

receiver <- sender [FINISH_TRANSACTION]
if (receiver.validTransaction(transaction)) {
    receiver.transactions.add(transaction);
    receiver -> sender [COMMIT_TRANSACTION]
} else {
    receiver -> sender [ROLLBACK_TRANSACTION]
}

```

### Example of block processing

```
A 10PTC
B 10PTC
C 10PTC

A -> B (1PTC)

A 9PTC (c1)  id = 2
B 11PTC (c1) id = 2
C 10PTC (c0) id = 1

B -> A (1PTC)

A 10PTC (c1)  id = 3
B 10PTC (c1) id = 3
C 10PTC (c0) id = 1


...


A -> B (1PTC)

A 10PTC (c1)  id = 8
B 10PTC (c1) id = 8
C 10PTC (c0) id = 3
```

### Design of Block

```

A 9PTC (c1)  id = 1
B 11PTC (c1) id = 1

A -> B (2PTC)

#### Block

tansaction0:
inputID:
    0
    1
Output:
    56

Amount:
    5

Sygnature:
    SigPrivate(hash(Input[0],Outputs,Amount))
    SigPrivate(hash(Input[1],Outputs,Amount))

Transaction1:
InputId:

Output:
    MinerAdd
Amount:
    51

Signature:


 - [A -> A (9PTC)]

output (B have done PoW):
 - [A -> B (2PTC)]
 - [A -> A (7PTC)]

 - [ -> B (0.1PTC)] (payment for PoW)
```

Block - list<Transaction>

Transaction:
list<String> inputID;
list<String> signature;

list<String> outputID;
list<String> outputAmount;