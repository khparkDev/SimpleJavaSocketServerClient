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
import java.util.StringTokenizer;

public class ReadSocket {
	private static final CharsetDecoder CAHRSET_DECODER = Charset.forName("UTF-8").newDecoder();
	private WindowPanelControl wpc;
	private Selector selector = null;

	public ReadSocket(Selector selector, WindowPanelControl wpc) {
		this.selector = selector;
		this.wpc = wpc;
	}

	public void start() {
		try {

			while (true) {
				selector.select();

				for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext();) {
					SelectionKey key = it.next();

					if (key.isReadable()) {
						read(key);
					}

					it.remove();
				}

				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
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
				e1.printStackTrace();
			}
		}

		buffer.flip();
		String data = "";

		try {
			data = CAHRSET_DECODER.decode(buffer).toString();
		} catch (CharacterCodingException e) {
			e.printStackTrace();
		}

		String senderId = "";
		String message = "";
		StringTokenizer st = new StringTokenizer(data, Constants.DELIMETER);
		int idx = 0;

		while (st.hasMoreTokens()) {
			String dt = st.nextToken();

			if (idx == 0) {
				senderId = dt;
			} else {
				message = dt;
			}

			idx++;
		}

		if (!wpc.getId().equals(senderId)) {
			wpc.setMessage(" " + senderId + " : " + message);
		}

		if (buffer != null) {
			buffer.clear();
			buffer = null;
		}
	}
}
