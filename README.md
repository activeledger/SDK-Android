# SDK-Android


## SDK Dev Instruction

Use the ActiveLedger SDK interface to Use the SDK.

## Initialise the SDK

```
ActiveLedgerSDK.getInstance().initSDK(this,"protocol","URL","port");
Example:
ActiveLedgerSDK.getInstance().initSDK(this,"http","testnet-uk.activeledger.io","5260");
```

## Generate KeyPair

```
KeyPair keyPair = ActiveLedgerSDK.getInstance().generateAndSetKeyPair(KeyType,SaveKeysToFile);
Example:
KeyPair keyPair = ActiveLedgerSDK.getInstance().generateAndSetKeyPair(keyType,true);
```

## Oboard KeyPair

```
ActiveLedgerSDK.getInstance().onBoardKeys(KeyPair, "KeyName");
Example:
ActiveLedgerSDK.getInstance().onBoardKeys(keyPair, "ActiveLedgerAwesomeKey");
```
