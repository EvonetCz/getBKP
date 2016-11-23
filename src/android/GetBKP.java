package cz.evonet.getbkp;

import android.util.Base64;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class GetBKP extends CordovaPlugin {

    public GetBKP() {};

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException, UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, SignatureException {
        if (action.equals("getKey")) {
            JSONObject result = getKey(args.getString(0), args.getString(1));
            callbackContext.success(result);
        } else {
            return false;
        }
        return true;
    }

    private JSONObject getKey(String result, String cert) throws NoSuchAlgorithmException, SignatureException, InvalidKeyException, UnsupportedEncodingException, InvalidKeySpecException, JSONException {

//        String result = args[0]; //"CZ00000019|273|/5546/RO24|0/6460/ZQ42|2016-01-05T00:30:12+01:00|34113.00";
//        String cert = args[1]; //MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQDFCIfnLl3YjNyxM3y2FAVovKQMetfyyj/lfLY3DoN1z/8gaVRfcqTZbwh9Btg0HafSmrWBvfgjEC/pG9HNawYZ9nPE+WIP9bXkoOfDTmmVtX4n6OLi2Di+U7+FmPJzykV0ujsOsfcGnQ0f63xZYoGJIwLJuz3gmAF//DfnOeTT7OUZeOKobBSYkQOKv1j05QqQZ7HP+5oq7+hNylFrjuEi5OAeVgJAYScE4COvcpqPKpb7OeR9f78knYFffg5zp/6bi6qkP5uGYEuuQvbW1mATjoqbAWz8c7HNA56uNFlz8V+z9bL0f/xwQjgy4d+5qelTX46tq0vJ2XM9dJaF8ytJAgMBAAECggEAcZ9ex9k8MyHgLqvTUiividum+q9oktFBEbTeW1eaRblBlc5H4pb5K45VJcxpp3wmiFPBMeV8D7RI/LOXRE9ggF5YGpH5k9yNHSARJta0GqpD6v3owQoRhuhCvOcbgdx2Oz8dyXalToIIzIx+9AjTTGMNO4onv7nIu6aWEliXdgHVvqisMpf6GZ6O5JnwFpR4NAZSqFEDHPJUiSa6s/oyecz/4oSqcla2zla6nCHb6m83vvb4jQ/AaoFfbCFv+oLJyZlJPGGHVyp1fvMnKV+OmLVaE7E78vXfvMvl3VHCi00phi3gPstjgSOqEsBWXGgoBIuvfdJkhOcBfWf5m90EgQKBgQDyaGnaCZuCSkb5axx7rpTQjdoBaOQcnnHVF9RrOBdH3E6ZJWrxBCi3N7Aivk2AAzUBTiKb/uRtf35fbeCiSmjXbVMjX42xFEPiJVEjqJRzmphqci/5Hq6PfB423a1TX4vECF/NRb9xbtp1IjqD9IkwUNG/Bx/BqPWq75P7FrTrmQKBgQDQFMyaxNBx47FzZmVVc2w3mF1fxv1m3t8o6RxT4G22eHczkZ8pOJT1UYCM8wnVEQPXmNGt85EdS7kkD4Imkyfc52MmX0pQNbDJXDvJYd5ZlQZ/zC9maY3ef+GZ2chs1j/0PA9Y9dvKro325RiGz+a+TWKutpRNbwqpOvLJrFaLMQKBgAtpKNpvq1dVwcOJ7DxSOoUauFFqq5pBRyB9z60AZfAnCbghz8fqpzQAthTcmm9VN1CJag2n0P7qintZg8J/+DFz3v8CR3w3dP6XPRuNmvdaJqSUHXf/nr34XL++baNIEx82ObRC/UEMs9Hhu5lskGyq0UTJxA/ssSvLvU6Lgha5AoGBAKo0CTSjvrkZ/WGepU7jTeaf2+jnFQnbTgDhxQka78MtAJwPBniqTrXnh9ZDSoydEV5+Iy09qTqkYPmNMfGptxarsl+F3HyFnmjm6ASO6FiwXJOWikMkHiacxgWZrabRDZkSs58Z5EICzB7jQE+tqVmKZSjyMZaxOLA6hrPOIzBRAoGAEwGiWaDbGxUoOzXsiF58CP2VuibPIYwMwJab9bPYphW2js6MGl34S5KQLjLRQldIoW0S7m/dACF2S4ba9DSefvbegDaZIc/GcZQzmZdpk1RfXJPofctuHAWxPkrA3JAD8uu31FnKLuaX31+THflznF6OfC6QUHjZJL/WrbiUMBM=";

        byte[] data = Base64.decode(cert);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(data);
        KeyFactory fact = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = fact.generatePrivate(spec);

        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(result.getBytes("UTF-8"));
        byte[] rsa_text = signature.sign();
        String PKP = Base64.encode(rsa_text);
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.update(rsa_text);
        byte[] bkp_text = digest.digest();
        StringBuilder sb = new StringBuilder(bkp_text.length * 2);
        for(byte b: bkp_text)
            sb.append(String.format("%02x", b & 0xff));
        String BKP = sb.toString();
        BKP = BKP.substring(0, 8) + "-" + BKP.substring(8, 16) + "-" + BKP.substring(16, 24) + "-" + BKP.substring(24, 32) + "-" + BKP.substring(32, 40);

        JSONObject res = new JSONObject();
        res.put("pkp", PKP);
        res.put("bkp", BKP);
        return res;
    }
}
