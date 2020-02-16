package com.khs.khsNettyClient.echo;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {
	public static void main(String[] args) {
		// 서버와 다르게 이벤트 루프 그룹을 하나만 설정한다.
		// 서버에 연결된 채널 하나만 존재하기 때문.
		EventLoopGroup group = new NioEventLoopGroup();
		
		Bootstrap b = new Bootstrap();
		b.group(group)
			.channel(NioSocketChannel.class)						// 클라이언트 애플리케이션이 생성하는 채널의 종류 설정. NIO로 동작하는 것으로.
			.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline p = ch.pipeline();
					p.addLast(new EchoClientHandler());
				} 	
			});
		
		try {
			// connect 	: ChannelFuture를 반환하는데, 비동기 메서드의 처리결과를 확인할 수 있다.
			// sync		: ChannelFuture 요청이 완료될 때 까지 대기. 요청이 실패하면 예외를 던진다.
			//				즉, connect 메소드 처리가 완료될 때 까지 다음 라인으로 진행하지 않음.
			ChannelFuture f = b.connect("localhost", 8888).sync();
			
			f.channel().closeFuture().sync();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}
	}
}
