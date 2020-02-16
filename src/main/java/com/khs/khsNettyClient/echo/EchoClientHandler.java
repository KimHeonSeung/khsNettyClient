package com.khs.khsNettyClient.echo;

import java.nio.charset.Charset;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoClientHandler extends ChannelInboundHandlerAdapter {
	private Logger logger = LogManager.getLogger(EchoClientHandler.class);
	
	// 소켓 채널이 최초 활성화되었을 때 실행
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// super.channelActive(ctx);
		logger.info("channelActive");
		String sendMessage = "Hello, Netty !";
		
		ByteBuf messageBuffer = Unpooled.buffer();
		messageBuffer.writeBytes(sendMessage.getBytes());
		
		StringBuilder builder = new StringBuilder();
		builder.append("전송한 문자열 [").append(sendMessage).append("]");
		
		logger.info(builder.toString());
		// 기록과 전송을 동시에 하는 메소드. write(기록) flush(전송)
		ctx.writeAndFlush(messageBuffer);
	}
	
	// 서버로부터 수신된 데이터가 있을 때 호출되는 메소드
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// super.channelRead(ctx, msg);
		logger.info("channelRead");
		
		String readMessage = ( (ByteBuf) msg ).toString(Charset.defaultCharset());
		
		StringBuilder builder = new StringBuilder();
		builder.append("수신한 문자열 [").append(readMessage).append("]");
		
		logger.info(builder.toString());
	}
	
	// 수신된 데이터를 모두 읽었을 때 호출되는 메소드
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// super.channelReadComplete(ctx);
		// 서버와 연결된 채널을 닫는다. 이후 송수신 채널은 닫히고 클라이언트 프로그램은 종료.
		ctx.close();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		// super.exceptionCaught(ctx, cause);
		ctx.close();
	}
}
