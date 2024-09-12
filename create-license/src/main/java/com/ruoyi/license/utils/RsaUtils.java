package com.ruoyi.license.utils;


public class RsaUtils {

    public static final String PRIVATE_KEY =
        "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAIsI4hM/zdj+kMddrSf93hMA1Cy4IrJwrXHRvlBRT"
            + "SybaxsZeW5mZuDyTBKRhpCAHSZ8hN2RP+eeyQ+CGiylinsFAvynl4ERJRI6r6sFqL/7xl1183iS/Xlv9ZfgoKuzW9jgJsFbs6BG9VGb5Z2w/0DDjlHlDvWZKo2ZLJ3n"
            + "/G/fAgMBAAECgYAlc0YaM0SKX7+1w/jhXdOUwNNmHE9US1+c2gducQfdrRuDmDAKWdD6ZKmzErnxcGDpvf64A+j3xeCYqc3NGuYa6VJbr2ANE5nHDf4pS0IzvkgcHnnw"
            + "ETEAmHvhRaOdXUF4ULDOPBa8cyih+lwHIOgdeAnS4q8YhpNuBOIcQUaRkQJBAL2jnSnWp8MKl+nyCb6aGdY8klqGR1sfGjs01dMGADh05Gt3wV3mcaXEEhj53QDAvU/b"
            + "Ys8nL4QTritxbmP+8ZcCQQC7r/0Dcve8IW3cl8L89d2NCiSTG+Mq8lCf7cK2czmVv1PnelwM3ohw0+XrZEUa7idVmn/g5tBIKk+f2PStHqz5AkB0n9pXkJs7Z5hlV2SG"
            + "w16AUl/vLAVWFJqdLrERRe98yOZw8QUKKl5aA2rd0UwI2n+STOlHXGHsZ+4E6Y1qEa+hAkA1vikG6c7CUhgxSEuPP5/XGQZsEVo1G/m2MdDFiaz9kjGmYMUm8jnDhQmb"
            + "I7UeBv/AZWMktTMC1wrBzNATGmuRAkA7JxIVHAsWJM/OpPjx7ICjSO+t36LTOM+XwVQwaFkXkC5DGIUjj7Na4YkM3z0VLjr/Arf3++NlQKjze5zYWiP+";
    public static final String PUBLIC_KEY =
        "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCLCOITP83Y/pDHXa0n/d4TANQsuCKycK1x0b5QUU0sm2sbGXluZmbg8kwSk"
            + "YaQgB0mfITdkT/nnskPghospYp7BQL8p5eBESUSOq+rBai/+8ZddfN4kv15b/WX4KCrs1vY4CbBW7OgRvVRm+WdsP9Aw45R5Q71mSqNmSyd5/xv3wIDAQAB";

    public static void main(String[] args) {
//        RSA rsa = new RSA(PRIVATE_KEY, PUBLIC_KEY);
//        LicenseParamVO licenseParamVO=new LicenseParamVO();
//        licenseParamVO.setBatchNo("123456789");
//        licenseParamVO.setLockNumber(5000);
//        List<String> aa=new ArrayList<>();
//        for(int i=0;i<50000;i++){
//            aa.add("1234567890qwerty");
//        }
//        licenseParamVO.setLockSerialNumberList(aa);
//        String encrypt2 = rsa.encryptHex(
//            StrUtil.bytes(JSON.toJSONString(licenseParamVO), CharsetUtil.CHARSET_UTF_8), KeyType.PrivateKey);
//        try (FileWriter writer = new FileWriter("D:\\out\\aa.license")) {
//            writer.write(encrypt2);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(encrypt2);
//        String decrypt2 = rsa.decryptStr(encrypt2, KeyType.PublicKey);
//        System.out.println(decrypt2);
    }
}
