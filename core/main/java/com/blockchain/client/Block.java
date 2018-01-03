package com.blockchain.client;

import com.blockchain.util.KeysUtil;
import com.blockchain.util.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Created by denis on 12/10/2017.
 */
public class Block {

    private final Configuration configuration;
    private int id;
    private long timestamp;

    private BlockData blockData;
    private byte[] signature;

    private byte[] prevHash;

    private int nonce;

    private Hashing hashing;

    static public Block create(
            Configuration configuration,
            int id,
            byte[] prevHash,
            List<Transaction> inputs,
            List<Transaction> outputs
    ) {
        checkNotNull(inputs, "input can't be null");
        checkNotNull(outputs, "output can't be null");
        checkNotNull(prevHash, "prevHash can't be null");

        checkArgument(inputs.size() > 0 || id == 0, "input can't be empty if this is not first block");
        checkArgument(outputs.size() > 0, "output can't be empty");

        return new Block(configuration, id, prevHash, inputs, outputs);
    }

    private Block(Configuration configuration, int id, byte[] prevHash, List<Transaction> inputs, List<Transaction> outputs) {
        this.configuration = configuration;
        this.id = id;
        this.prevHash = prevHash;
        blockData = new BlockData(inputs, outputs);
        timestamp = System.currentTimeMillis();

        hashing = new Hashing(configuration.getComplexity()); // make configurable
    }

    public int getId() {
        return id;
    }

    public int getNonce() {
        return nonce;
    }

    public byte[] getHash() {
        return hashing.hash(nonce + getBlockBody() + signature);
    }

    public boolean isValid() {
        return hashing.isValid(nonce + getBlockBody() + signature);
    }

    public byte[] getPrevHash() {
        return prevHash;
    }

    private String getBlockBody() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(timestamp);
        builder.append(prevHash.toString());

        try {
            // blockData should be validated!!!
            builder.append(StringUtil.serializeToString(blockData));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    @Override
    public String toString() {
        return "Block{nonce=" + nonce + '}';
    }

    public void calcNonce() {
        Objects.requireNonNull(signature, "Block should be signed");
        nonce = hashing.calcNonce(getBlockBody() + signature);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Block block = (Block) o;

        if (id != block.id) return false;
        if (timestamp != block.timestamp) return false;
        if (nonce != block.nonce) return false;
        if (!blockData.equals(block.blockData)) return false;
        if (!Arrays.equals(signature, block.signature)) return false;
        return Arrays.equals(prevHash, block.prevHash);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (int) (timestamp ^ (timestamp >>> 32));
        result = 31 * result + blockData.hashCode();
        result = 31 * result + Arrays.hashCode(signature);
        result = 31 * result + Arrays.hashCode(prevHash);
        result = 31 * result + nonce;
        return result;
    }

    public boolean sign(PrivateKey privateKey) {
        try {
            signature = KeysUtil.sign(getBlockBody(), privateKey);
            return true;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean verify(PublicKey publicKey) {
        try {
            return KeysUtil.verify(getBlockBody(), signature, publicKey);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Transaction> getInputTransactions() {
        return Collections.unmodifiableList(blockData.getInputs());
    }

    public List<Transaction> getOutputTransactions() {
        return Collections.unmodifiableList(blockData.getOutputs());
    }
}