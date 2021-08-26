package Server;

import TLV.Msg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ProcessingHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        Msg requestData = (Msg) msg;
//        FileInputStream fis = new FileInputStream("D:\\privateKey.rsa");
//		byte[] b = new byte[fis.available()];
//		fis.read(b);
//		fis.close();
//
//		// Tạo private key
//		PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(b);
//		KeyFactory factory = KeyFactory.getInstance("RSA");
//		PrivateKey priKey = factory.generatePrivate(spec);
//
//		// Giải mã dữ liệu
//		Cipher c = Cipher.getInstance("RSA");
//		c.init(Cipher.DECRYPT_MODE, priKey);
//		byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(requestData.getMsg()));
//		System.out.println("Dữ liệu sau khi giải mã: " + new String(decryptOut));
        System.out.println(requestData.getType()+": "+requestData.getMsg());
        Msg m = new Msg();
        m.setType(requestData.getType());
        m.setMsg("Hello");
        ctx.writeAndFlush(m);
        
    }
}
