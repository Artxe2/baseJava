package blockchain.node;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class BcNodeP2PThread implements Runnable {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BcNodeP2PThread.class);
	private Socket p2pTCP;
	private ObjectOutputStream p2pOos = null;
	private ObjectInputStream p2pOis = null;
	private BcNode node;

	protected BcNodeP2PThread(BcNode node, Socket p2pTCP) {
		this.node = node;
		this.p2pTCP = p2pTCP;
	}

	@Override
	public void run() {
		BcNode.peerList.add(this);
		try {
			p2pOos = new ObjectOutputStream(p2pTCP.getOutputStream());
			p2pOis = new ObjectInputStream(p2pTCP.getInputStream());
			boolean isLoop = true;
			logger.info("BcNp2pThread Start " + BcNode.peerList.size());
			while (isLoop) {
				try {
					BcP2PDto request = (BcP2PDto) p2pOis.readObject();

					String response = node.analysisRequest(request);
					logger.info("response: " + response);
					// 잘못된 노드를 발견했을 경우 연결을 끊는다.
					if (BcP2PVo.EVIL_DETECTION.equals(response)) {
						isLoop = false;
						// 블록리스트를 받아야할 경우 자신의 해시리스트를 전송하며 블록리스트를 요청한다.
					} else if (BcP2PVo.GIVE_ME_BLOCK_LIST.equals(response)) {
						BcP2PDto dto = new BcP2PDto(response, node.getHashList());
						broadCast(dto);
						// 블록리스트를 요청받았을 경우 블록리스트를 전송한다.
					} else if (BcP2PVo.BLOCK_LIST.equals(response)) {
						ArrayList<String> bList = node.analysisHashList((List<String>) request.getData());
						if (bList != null) {
							logger.info("bList.size: " + bList.size());
							BcP2PDto dto = new BcP2PDto(response, bList);
							broadCast(dto);
						}
					}
					//					else if (BcP2PVo.CHAIN_LENGTH.equals(response)) {
					//						BcP2PDto dto = new BcP2PDto(response, node.chain.size());
					//						broadCast(dto);
					//					}

				} catch (Exception e) {
					isLoop = false;
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			BcNode.peerList.remove(this);
			try {
				if (p2pOis != null)
					p2pOis.close();
				if (p2pOos != null)
					p2pOos.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
			}
			logger.info("BcNp2pThread End");
		}
	}

	public void broadCast(BcP2PDto dto) {
		logger.info("broadCast: " + dto.getHeader());
		try {
			p2pOos.writeObject(dto);
			p2pOos.flush();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
