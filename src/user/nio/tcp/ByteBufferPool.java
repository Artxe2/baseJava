package user.nio.tcp;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

public class ByteBufferPool {

	/*
	 * ByteBuffer flip: lim = pos, pos = 0; rewind: pos = 0; clear: lim = cap, pos =
	 * 0;
	 */

	private static final List<ByteBuffer> stack = new ArrayList<>();

	protected ByteBufferPool(int stackSize, int bufferSize) {
		initBuffer(stackSize, bufferSize);
	}

	private void initBuffer(int stackSize, int bufferSize) {
		ByteBuffer stackBody = ByteBuffer.allocateDirect(bufferSize * stackSize);
		stackBody.flip();
		int position = 0;
		for (int i = 0; i < stackSize; i++) {
			stackBody.position(position);
			stackBody.limit(position += bufferSize);
			stack.add(stackBody.slice());
		}
	}

	protected synchronized ByteBuffer burrowBuffer() {
		int index = -1;
		while (index < 0) {
			index = stack.size() - 1;
			try {
				Thread.sleep(0, 1);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
		}
		return stack.remove(index);
	}

	protected synchronized void returnBuffer(ByteBuffer bb) {
		bb.clear();
		stack.add(bb);
	}
}
