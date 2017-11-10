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