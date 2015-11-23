package com.khpark.cleint;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;

public class ReadSocket {
	private static final Charset charset = Charset.forName("UTF-8");;
	private static final CharsetDecoder decoder = charset.newDecoder();;
	private Selector selector = null;

	public ReadSocket(Selector selector) {
		this.selector = selector;
	}

	public void start() {
		System.out.println("Reader is started..");

		try {

			while (true) {
				System.out.print("[ message ] : ");
				selector.select();
				Iterator<SelectionKey> it = selector.selectedKeys().iterator();

				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();

					if (key.isReadable()) {
						read(key);
					}

					it.remove();
				}

				System.out.println();
			}
		} catch (Exception e) {
			System.out.println("SimpleChatClient.startServer()");
		}
	}

	private void read(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer buffer = ByteBuffer.allocateDirect(1024);

		try {
			socketChannel.read(buffer);
		} catch (IOException e) {
			try {
				socketChannel.close();
			} catch (IOException e1) {
			}
		}

		buffer.flip();
		String data = "";

		try {
			data = decoder.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			System.out.println("SimpleChatClient.read()");
		}

		System.out.println("[Received Message ] : " + data);

		if (buffer != null) {
			buffer.clear();
			buffer = null;
		}
	}
}
