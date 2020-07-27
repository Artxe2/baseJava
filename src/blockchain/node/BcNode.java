package blockchain.node;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.BindException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.PublicKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class BcNode extends JFrame implements Serializable, ActionListener {
	private static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BcNode.class);
	public static boolean isRun;
	// https://brunch.co.kr/@chunja07/41
	// https://brunch.co.kr/@chunja07/42
	// https://github.com/CryptoKass/NoobChain-Tutorial-Part-2/tree/master/src/noobchain
	// https://m.blog.naver.com/sun2lub/221383390325
	// https://steemit.com/kr/@yahweh87/4-merkle-tree-merkle-root

	/* 블록체인 기능 객체 > */

	public static String block_path;
	public static String wallet_path;
	public static String publickey_path;
	public static String transaction_path;
	private static String god_path;
	private static String root_path;
	public static String tx_list_path;
	private static JFileChooser jfc_w;
	private static JFileChooser jfc_p;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	public static boolean isAutoMining = false;
	public static boolean isMiningable = true;
	public static Wallet walletS;// 주는 사람
	public static PublicKey publicKeyR;// 받는 사람
	public static PublicKey publicKeyM;// 채굴하는 사람

	/*
	 * < 블록체인 기능 객체
	 *
	 * 블록체인 구성 객체 >
	 */

	public static final List<Block> chain = new Vector<>();// Block "Chain"
	public static final Map<PublicKey, Map<String, TxOutput>> uTxOs = new ConcurrentHashMap<>();// 전체 코인목록
	// get(PublicKey).get(txOId)
	public static final Map<PublicKey, Map<String, TxOutput>> uTxIs = new ConcurrentHashMap<>();// 거래 대기중인 코인목록
	public static final Map<String, TxOutput> aTxOs = new ConcurrentHashMap<>();// 최초부터 현재까지 생성되었던 코인목록 get(txOId)
	public static final Map<String, Transaction> unprocessedTxs = new ConcurrentHashMap<>();// 체인에 연결될 예정인 트랜잭션 목록
	public static final Map<String, Transaction> processedTxs = new ConcurrentHashMap<>();// 체인에 연결된 트랜잭션 목록
	public static final Map<String, Integer> txIndexs = new ConcurrentHashMap<>();// 트랜잭션이 기록된 블록 index 목록 get(txId)
	public static Wallet god;// 채굴 보상 주는 사람
	public static final int difficulty = 2000;// 최소 채굴 난이도
	public static final long guideTime = 2000;// 2초
	public static final BigDecimal minTx = new BigDecimal("0.0001");// 트랜잭션 최소단위
	public static final BigDecimal rewardValue = new BigDecimal(10);
	public static final BigDecimal fees = new BigDecimal("0.00");// 수수료
	//	public static Block blockInRecord;// 현재 트랜잭션을 기록중인 아직 채굴되지않은 블록
	public static int savedBlockCount;

	/*
	 * < 블록체인 구성 객체
	 *
	 * 화면 관련 객체 >
	 */

	private static final JPanel jp_info = new JPanel();
	private static final JLabel jlb_info_se = new JLabel("주는 사람: 0만원");
	private static final JLabel jlb_info_re = new JLabel("받는 사람: 0만원");
	private static final JLabel jlb_info_ms = new JLabel("Miner: 0만원");
	protected static final JTextArea jta = new JTextArea();
	private static final Font jtaFont = new Font(Font.DIALOG, Font.BOLD, 42);
	private static final JScrollPane jsp = new JScrollPane(jta);
	private static final JPanel jp_btns = new JPanel();
	private static final JButton jbtn_ps = new JButton("공개키 저장");
	private static final JButton jbtn_am = new JButton("자동 채굴");
	private static final JButton jbtn_wc = new JButton("지갑 생성");
	private static final JButton jbtn_bc = new JButton("블록 채굴");
	private static final JPanel jplb = new JPanel();
	private static final JLabel jlb_ms = new JLabel("      Miner ↓");
	private static final JButton jbtn_ms = new JButton("Miner");
	private static final JPanel jp_tx = new JPanel();
	private static final JButton jbtn_se = new JButton("주는 사람");
	private static final JLabel jlb_se = new JLabel("(이)가 ");
	private static final JButton jbtn_re = new JButton("받는 사람");
	private static final JLabel jlb_re = new JLabel("에게 ");
	private static final JTextField jtf = new JTextField(5);
	private static final JLabel jlb_tx = new JLabel("코인을 ");
	private static final JButton jbtn_tx = new JButton("준다.");
	private static final Font font = new Font(Font.DIALOG, Font.BOLD, 42);
	private static final JPanel jp_north = new JPanel();
	private static final JPanel jp_north2 = new JPanel();
	private static final JPanel jp_north3 = new JPanel();
	private static final JPanel jp_west = new JPanel();
	public static final Map<PublicKey, String> keyName = new HashMap<>();// 디버깅용 WalletName 맵

	/*
	 * < 화면 관련 객체
	 *
	 * 네트워크 관련 객체 >
	 */

	private static final int NETWORK_PORT = 9999;
	public static final int P2P_PORT = 11111;
	protected Socket csTCP = new Socket();// 네트워크와 연결되는 소켓
	private ServerSocket ssTCP = null;// p2p 연결을 위한 서버 소켓
	protected Socket p2pTCP = new Socket();// p2p 연결을 위한 소켓
	protected static final List<BcNodeP2PThread> peerList = new Vector<>();

	/*
	 * < 네트워크 관련 객체
	 *
	 * 체인 기본 기능 코드 >
	 */

	public BcNode() {
		setPath();
		connectServer();
		initEnvironment();
		initDisplay();
	}

	private void setPath() {
		StringTokenizer st = new StringTokenizer(getClass().getClassLoader().getResource("blockchain/save").toString(),
				"/");
		st.nextToken();
		StringBuilder sb = new StringBuilder();
		while (st.hasMoreTokens()) {
			sb.append(st.nextToken() + "\\");
		}
		root_path = sb.toString();
		if (root_path.contains("target\\classes")) {
			String[] sa = root_path.split("target\\\\classes");
			System.out.println(sa[0]);
			root_path = sa[0] + "src\\main\\java" + sa[1];
		}
		block_path = root_path + "block\\";
		wallet_path = root_path + "wallet\\";
		publickey_path = root_path + "public_key\\";
		transaction_path = root_path + "transaction\\";
		tx_list_path = root_path + "TxList.tx_list";
		god_path = root_path + "God.god";
		logger.info("BLOCK_PATH: " + block_path);
		logger.info("WALLET_PATH: " + wallet_path);
		logger.info("PUBLICKEY_PATH: " + publickey_path);
		logger.info("TRANSACTION_PATH: " + transaction_path);
		logger.info("TX_LIST_PATH: " + tx_list_path);
		logger.info("GOD_PATH: " + god_path);
		jfc_w = new JFileChooser(wallet_path);
		jfc_p = new JFileChooser(publickey_path);
	}

	private void initEnvironment() {
		Security.addProvider(new BouncyCastleProvider());
		File f = new File(god_path);
		if (f.exists()) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
				god = (Wallet) ois.readObject();
			} catch (Exception e) {
				jta.append("god-Wallet 읽기 실패\n");
				e.printStackTrace();
			}
		} else {
			try {
				Wallet imGod = new Wallet();
				oos = new ObjectOutputStream(new FileOutputStream(f));
				oos.writeObject(imGod);
				god = imGod;
			} catch (Exception e) {
				jta.append("god-Wallet 생성 실패\n");
				e.printStackTrace();
			} finally {
				try {
					if (oos != null)
						oos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		uTxOs.put(god.publicKey, new HashMap<String, TxOutput>());
		uTxIs.put(god.publicKey, new HashMap<String, TxOutput>());
		loadBlocks(0);
	}

	public static void main(String[] args) {
		setDefaultLookAndFeelDecorated(true);
		new BcNode();// test() 실행
	}

	// 체인 유효성 검사
	protected int isChainValid() {
		Block pBlock;
		Block cBlock;

		Map<PublicKey, Map<String, TxOutput>> tempUTxOs = new HashMap<>();// 검증 완료된 트랜잭션 목록
		tempUTxOs.put(god.publicKey, new HashMap<String, TxOutput>());

		// 모든 블록의 유효성 검사
		int b = 0;
		for (; b < chain.size(); b++) {
			cBlock = chain.get(b);// 현재 블록
			if (b > 0) {
				pBlock = chain.get(b - 1);// 이전 블록
				// 현재 블록에 기록된 이전 블록의 해쉬코드가 실제 이전 블록의 해쉬코드와 다를 경우
				if (b > 0 && !pBlock.hash.equals(cBlock.previousHash)) {
					logger.error("블록의 이전 블록 해시코드가 실제 이전 블록의 해시코드와 다릅니다. at " + b);
					break;
				}
				// 현재 블록의 난이도가 이전 블록의 난이도에 따라 적용되었어야할 난이도와 다를 경우
				if (HashUtil.getDifficulty(pBlock.difficulty, guideTime, pBlock.timeStamp,
						cBlock.timeStamp) != cBlock.difficulty) {
					logger.error("블록의 난이도가 정상적이지 않습니다. at " + b);
					break;
				}
			}
			// 현재 블록의 해쉬코드가 다시 계산한 해쉬코드와 다를 경우
			if (!cBlock.hash.equals(cBlock.calcHash(0))) {
				logger.error("블록의 해시코드가 계산된 결과값과 다릅니다. at " + b);
				break;
			}

			// 현재 블록의 마이닝되지 않은 경우
			if (!HashUtil.verifyMining(cBlock.difficulty, cBlock.hash)) {
				logger.info("블록이 제대로 채굴되지 않았습니다. at " + b);
				break;
			}
			// 현재 블록의 머클루트가 유효하지 않은 경우
			if (!HashUtil.verifyMerkleRoot(cBlock.merkleRoot, cBlock.txs)) {
				logger.info("블록의 거래목록이 변조되었습니다. at " + b);
				break;
			}

			TxOutput tempOutput = null;
			// 현재 블록의 트랜잭션들의 유효성 검사
			try {
				for (int t = 0; t < cBlock.txs.size(); t++) {
					Transaction cTx = cBlock.txs.get(t);// 현재 트랜잭션
					// 트랜잭션의 싸인이 유효하지 않으면서 첫 번째 트랜잭션이 아니거나 채굴보상이 아닌 경우
					if (!ChainUtil.verifySignature(cTx)) {
						logger.info("거래번호 (" + t + ") 의 싸인이 유효하지 않습니다. at " + b);
						// return b;
						break;
					}
					// 트랜잭션의 해쉬 아이디가 다시 계산한 해쉬 아이디가 아닐 경우\
					if (!cTx.txId.equals(cTx.calcHash())) {
						logger.info("거래번호 (" + t + ")의 해시코드가 계산된 결과값과 다릅니다. at " + b);
						break;
					}
					// 트랜잭션의 인풋리스트의 유효성 검사
					for (String id : cTx.inputs) {
						tempOutput = tempUTxOs.get(cTx.sender).get(id);
						if (tempOutput == null) {
							logger.info("거래번호 (" + t + ")에 사용된 코인이 유효하지 않습니다. at " + b);
							break;
						}
						tempUTxOs.get(cTx.sender).remove(id);// 현재 인풋에 문제가 없으면 인풋에 기입된 아웃풋을 목록에서 제거
					}
					// 트랜잭션으로 추가된 아웃풋들을 목록에 추가
					for (String id : cTx.outputs) {
						TxOutput o = aTxOs.get(id);
						if (tempUTxOs.get(o.reciepient) == null) {
							tempUTxOs.put(o.reciepient, new HashMap<String, TxOutput>());
						}
						tempUTxOs.get(o.reciepient).put(o.id, o);
					}

					// 트랜잭션의 인풋과 아웃풋이 다를 경우
					if (!cTx.sender.equals(god.publicKey)
							&& ChainUtil.measureTxOsValues(cTx.inputs) != ChainUtil.measureTxOsValues(cTx.outputs)) {
						logger.info("거래번호 (" + t + ")의 금액이 일치하지 않습니다. at " + b);
						logger.info("inputs: " + ChainUtil.measureTxOsValues(cTx.inputs) + " != outs: "
								+ ChainUtil.measureTxOsValues(cTx.outputs));
						break;
					}

					// 트랜잭션의 코인을 받는 사람이 트랜잭션에 명시된 받는 사람이 아닐 경우
					if (!aTxOs.get(cTx.outputs.get(0)).reciepient.equals(cTx.recipient)) {
						logger.info("거래번호(" + t + ")의 대상이 잘못되었습니다.(입금) at " + b);
						break;
					}
					// 거래 수수료를 받는 사람이 채굴한 사람이 아닐 경우
					if (!aTxOs.get(cTx.outputs.get(1)).reciepient.equals(cBlock.miner)) {
						logger.info("거래번호(" + t + ")의 대상이 잘못되었습니다(수수료) at " + b);
						break;
					}
					// 거스름돈이 있는데 거스름돈을 받는 사람이 보내는 사람이 아닐 경우
					if (cTx.outputs.size() > 2 && !aTxOs.get(cTx.outputs.get(2)).reciepient.equals(cTx.sender)) {
						logger.info("거래번호(" + t + ")의 대상이 잘못되었습니다(잔액) at " + b);
						break;
					}
					// 지불금액, 수수료, 거스름돈 이외의 코인이 있는 경우
					if (cTx.outputs.size() > 3) {
						logger.info("거래번호(" + t + ")에 알 수 없는 대상이 추가되었습니다. " + b);
						break;
					}
				}
			} catch (NullPointerException e) {
				break;
			}
		}
		BigDecimal utxoCount = new BigDecimal(0);
		for (Map.Entry<PublicKey, Map<String, TxOutput>> m : tempUTxOs.entrySet()) {
			for (Map.Entry<String, TxOutput> e : m.getValue().entrySet()) {
				utxoCount = utxoCount.add(e.getValue().value);
			}
		}
		logger.info("info #Chain#Blockchain is valid, Chain's length: " + chain.size() + ", utxoCount: " + utxoCount);
		return b;
	}

	/*
	 * < 체인 기본 기능 코드
	 *
	 * 체인 화면 기능 코드 >
	 */

	private void initDisplay() {
		System.out.println(getClass().getClassLoader().toString());
		keyName.put(god.publicKey, "[GM] god");
		jbtn_bc.addActionListener(this);
		jbtn_ms.addActionListener(this);
		jbtn_am.addActionListener(this);
		jbtn_ps.addActionListener(this);
		jbtn_re.addActionListener(this);
		jbtn_wc.addActionListener(this);
		jbtn_se.addActionListener(this);
		jbtn_tx.addActionListener(this);
		setIconImage(new ImageIcon("src//main//java//blockchain//icon.png").getImage());
		setTitle("Chain Node ver 0.4 - OFF");
		setSize(1200, 800);
		setLocationRelativeTo(null);
		add("North", jp_north);
		jp_north.setLayout(new GridLayout(1, 2));
		jp_north.setBackground(new Color(59, 89, 152));
		jp_north.add(new JPanel() {
			@Override
			protected void paintComponent(java.awt.Graphics g) {
				g.drawImage(new ImageIcon(root_path + "img.png").getImage(), 0, 0, 950, 156, null);
				setOpaque(false);
			}
		});
		jp_north.add(jp_north3);
		jp_north3.setLayout(new BorderLayout());
		JLabel jlbtemp2 = new JLabel("여백의 미");
		jlbtemp2.setForeground(new Color(59, 89, 152));
		jlbtemp2.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		jlbtemp2.setForeground(new Color(59, 89, 152));
		jp_north3.add("North", jlbtemp2);
		jp_north3.add("Center", jp_north2);
		jp_north3.setBackground(new Color(59, 89, 152));
		jp_north2.setLayout(new GridLayout(2, 1));
		jp_north2.setBackground(new Color(59, 89, 152));
		jp_north2.add(jp_info);
		jp_info.setLayout(new GridLayout(1, 2));
		jp_info.setBackground(new Color(59, 89, 152));
		jp_info.add(jlb_info_se).setFont(font);
		jlb_info_se.setForeground(Color.white);
		jp_info.add(jlb_info_re).setFont(font);
		jlb_info_re.setForeground(Color.white);
		jp_north2.add(jlb_info_ms).setFont(font);
		JLabel jlbtemp = new JLabel("여백의 미");
		jlbtemp.setForeground(new Color(59, 89, 152));
		jlbtemp.setFont(new Font(Font.DIALOG, Font.BOLD, 20));
		jp_north3.add("South", jlbtemp);
		add("Center", jsp);
		jta.setFont(jtaFont);
		add("East", jp_btns);
		jp_btns.setLayout(new GridLayout(6, 1));
		jp_btns.add(jbtn_ps).setFont(font);
		jbtn_ps.setBackground(new Color(59, 89, 152));
		jbtn_ps.setForeground(Color.white);
		jp_btns.add(jbtn_wc).setFont(font);
		jbtn_wc.setBackground(new Color(59, 89, 152));
		jbtn_wc.setForeground(Color.white);
		jp_btns.add(jbtn_am).setFont(font);
		jbtn_am.setBackground(new Color(59, 89, 152));
		jbtn_am.setForeground(Color.white);
		jp_btns.add(jbtn_bc).setFont(font);
		jbtn_bc.setBackground(new Color(59, 89, 152));
		jbtn_bc.setForeground(Color.white);
		jp_btns.add(jplb);
		jplb.setLayout(new GridLayout(2, 1));
		jplb.add(new JLabel());
		jplb.setBackground(Color.white);
		jplb.add(jlb_ms).setFont(font);
		jlb_info_ms.setForeground(Color.white);
		jlb_ms.setForeground(new Color(59, 89, 152));
		jp_btns.add(jbtn_ms).setFont(font);
		jbtn_ms.setBackground(new Color(59, 89, 152));
		jbtn_ms.setForeground(Color.white);
		add("South", jp_tx);
		jp_tx.setBackground(new Color(59, 89, 152));
		jp_tx.add(jbtn_se).setFont(font);
		jbtn_se.setBackground(Color.white);
		jbtn_se.setForeground(new Color(59, 89, 152));
		jp_tx.add(jlb_se).setFont(font);
		jlb_se.setForeground(Color.white);
		jp_tx.add(jbtn_re).setFont(font);
		jbtn_re.setBackground(Color.white);
		jbtn_re.setForeground(new Color(59, 89, 152));
		jp_tx.add(jlb_re).setFont(font);
		jlb_re.setForeground(Color.white);
		jp_tx.add(jtf).setFont(font);
		jtf.setForeground(new Color(59, 89, 152));
		jp_tx.add(jlb_tx).setFont(font);
		jlb_tx.setForeground(Color.white);
		jp_tx.add(jbtn_tx).setFont(font);
		jbtn_tx.setBackground(Color.white);
		jbtn_tx.setForeground(new Color(59, 89, 152));
		add("West", jp_west);
		jp_west.setBackground(new Color(59, 89, 152));
		setVisible(true);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
	}

	// 디스플레이의 두 지갑이 가지고있는 코인을 갱신
	public static void reFreshBalance() {
		if (walletS != null) {
			jlb_info_se
			.setText(keyName.get(walletS.publicKey) + ": " + ChainUtil.measureWallet(walletS.publicKey) + "만원");
		}
		if (publicKeyR != null) {
			jlb_info_re.setText(keyName.get(publicKeyR) + ": " + ChainUtil.measureWallet(publicKeyR) + "만원");
		}
		if (publicKeyM != null) {
			jlb_info_ms.setText(keyName.get(publicKeyM) + ": " + ChainUtil.measureWallet(publicKeyM) + "만원");
		}
	}

	// 자동 채굴
	private void autoMining() {
		if ((isAutoMining = !isAutoMining)) {
			isMiningable = true;
			setTitle("Chain Node ver 0.4 - ON");
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						while (isAutoMining) {
							jta.append("자동 채굴중...\n");
							boolean isSuccess = createBlock();
							//							if (isAutoMining) {
							//								isAutoMining = isSuccess;
							//							}
						}
						if (!isAutoMining) {
							jta.append("자동 채굴 종료\n");
							setTitle("Chain Node ver 0.4 - OFF");
						}
					} catch (Exception e) {
						isAutoMining = false;
						jta.append("자동 채굴 종료\n");
						setTitle("Chain Node ver 0.4 - OFF");
						e.printStackTrace();
					}
				}
			}).start();
		} else {
			isMiningable = false;
			jta.append("자동 채굴 종료\n");
			setTitle("Chain Node ver 0.4 - OFF");
		}
	}

	// 새로운 블록의 유효성을 검사하고 유효할 경우 트랜잭션목록을 처리하고 체인에 연결
	private synchronized boolean chainingBlock(Block newBlock, boolean isLoad, boolean isSave) {
		logger.info("chainingBlock()");
		if (chain.size() > 0) {
			Block lastBlock = chain.get(chain.size() - 1);
			if (!lastBlock.hash.equals(newBlock.previousHash)) {
				logger.error("(!lastBlock.hash.equals(newBlock.previousHash))");
				return false;
			}
			if (HashUtil.getDifficulty(lastBlock.difficulty, guideTime, lastBlock.timeStamp,
					newBlock.timeStamp) != newBlock.difficulty) {
				logger.error(
						"(HashUtil.getDifficulty(lastBlock.difficulty, guideTime, lastBlock.timeStamp, newBlock.timeStamp) != newBlock.difficulty)");
				return false;
			}
		}
		if (!newBlock.hash.equals(newBlock.calcHash(0))) {
			logger.error("(!newBlock.hash.equals(newBlock.calcHash(0)))");
			return false;
		}
		if (!HashUtil.verifyMining(newBlock.difficulty, newBlock.hash)) {
			logger.error("(!HashUtil.verifyMining(newBlock.difficulty, newBlock.hash))");
			return false;
		}
		if (!HashUtil.verifyMerkleRoot(newBlock.merkleRoot, newBlock.txs)) {
			logger.error("(!HashUtil.verifyMerkleRoot(newBlock.merkleRoot, newBlock.txs))");
			return false;
		}
		try {
			for (Transaction t : newBlock.txs) {
				if (t == null) {
					logger.error("(Transaction t : newBlock.txs)");
					return false;
				}
				if (processedTxs.containsKey(t.txId)) {
					logger.error("(processedTxs.contains(t))");

					return false;
				}
				if (!ChainUtil.verifySignature(t)) {
					logger.error("(!ChainUtil.verifySignature(t))");
					return false;
				}
				if (!t.txId.equals(t.calcHash())) {
					logger.error("(!t.txId.equals(t.calcHash()))");
					return false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		if (!newBlock.processTxs(isLoad)) {
			logger.error("(!newBlock.processTxs(" + isLoad + "))");
			return false;
		}
		chain.add(newBlock);
		if (isSave) {
			saveBlocks(chain.size() - 1);
		}
		reFreshBalance();
		return true;
	}

	private boolean createBlock() {
		logger.info("createBlock()");
		Block newBlock;
		// 미처리된 트랜잭션들을 블록에 추가하고 채굴 시작
		if (chain.size() > 0) {
			newBlock = new Block(chain.get(chain.size() - 1));
		} else {
			newBlock = new Block(null);
		}
		logger.info("next difficulty: " + newBlock.difficulty);
		jta.append("채굴 시작, 다음 난이도: " + newBlock.difficulty + "\n");
		for (Map.Entry<String, Transaction> e : unprocessedTxs.entrySet()) {
			ChainUtil.addTransaction(newBlock, e.getValue());
		}

		isMiningable = true;
		if (publicKeyM == null) {
			jta.append("채굴하는 사람을 설정하세요.\n");
			return false;
		}

		int nonce = newBlock.mineBlock(publicKeyM);
		if (nonce > 0 && BcNode.chain.size() == newBlock.chainCount) {

			broadCastAll(BcP2PVo.NEW_BLOCK, newBlock);

			return chainingBlock(newBlock, false, true);
		} else {
			return false;
		}
	}

	private void removeBlock(int verifyChainLength, boolean isDel) {
		int i = verifyChainLength;
		jta.append("블록 삭제. " + i + "번부터 " + chain.size() + "번까지...\n");
		while (i < chain.size()) {
			Block b = chain.remove(chain.size() - 1);
			for (Transaction t : b.txs) {
				ChainUtil.rollbackTransaction(t);
			}
		}
		if (isDel) {
			// 유효하지 않은 블록 전부 삭제
			File f = new File(block_path + i + ".block");
			while (f.exists()) {
				f.delete();
				f = new File(block_path + ++i + ".block");
			}
		}
	}

	// 폴더에 있는 블록 파일들을 전부 읽어 체인에 연결
	private int loadBlocks(int index) {
		jta.append("블록 로딩 시작...\n");
		int i = index;
		if (i == 0) {
			chain.clear();
			uTxOs.clear();
			uTxIs.clear();
			aTxOs.clear();
			unprocessedTxs.clear();
			processedTxs.clear();
		}
		File f = new File(block_path + i + ".block");
		try {
			while (f.exists()) {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
				chainingBlock((Block) ois.readObject(), true, false);
				f = new File(block_path + ++i + ".block");
			}
			jta.append(i + "개의 블록이 로딩되었습니다.\n");
			if (i > 0) {

				try {
					ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(tx_list_path))));
					Map<String, Transaction> tempTxs = (Map<String, Transaction>) ois.readObject();
					for (Map.Entry<String, Transaction> e : tempTxs.entrySet()) {
						ChainUtil.isTransactionValid(e.getValue());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 체인의 유효성을 검사하여 유효한 체인의 길이가 현재 체인의 길이보다 짧을 경우
			// 유효하지 않은 체인을 모두 삭제
			int cv = isChainValid();
			if (cv < chain.size()) {
				removeBlock(cv, true);
			}
			reFreshBalance();
		}
		return i;
	}

	// 체인에 들어있는 블록들을 전부 파일로 내보내기
	private void saveBlocks(int start) {
		jta.append("블록 저장...\n");
		File f = null;
		try {
			int i = start;
			for (; i < chain.size(); i++) {
				f = new File(block_path + i + ".block");
				oos = new ObjectOutputStream(new FileOutputStream(f));
				oos.writeObject(chain.get(i));
			}
			jta.append(i + "번 블록 저장 완료.\n");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 선택된 지갑의 공개키 저장
	private void savePublicKey() {
		try {
			if (walletS != null) {
				File f = new File(publickey_path + keyName.get(walletS.publicKey) + ".public_key");
				oos = new ObjectOutputStream(new FileOutputStream(f));
				oos.writeObject(walletS.publicKey);
			}
			jta.append("공개키 저장 완료\n");
		} catch (Exception e) {
			jta.append("공개키 저장 실패\n");
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// 새 지갑 생성
	private void createWallet() {
		String name = JOptionPane.showInputDialog("Input your name");
		if (name != null) {
			Wallet wallet = new Wallet();
			try {
				File f = new File(wallet_path + name + ".wallet");
				oos = new ObjectOutputStream(new FileOutputStream(f));
				oos.writeObject(wallet);
				f = new File(publickey_path + name + ".public_key");
				oos = new ObjectOutputStream(new FileOutputStream(f));
				oos.writeObject(wallet.publicKey);
				jta.append("지갑 생성 완료. " + name + "의 지갑이 생성되었습니다.\n");

				broadCastAll(BcP2PVo.KEY_NAME, new KeyName(wallet.publicKey, name));
			} catch (Exception e) {
				jta.append("지갑 생성 실패\n");
				e.printStackTrace();
			} finally {
				try {
					if (oos != null)
						oos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	// 주는 사람 선택
	private void selectSender() {
		jta.append("주는 사람 선택하기...\n");
		try {
			jfc_w.showOpenDialog(null);
			File f = jfc_w.getSelectedFile();
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
				walletS = (Wallet) ois.readObject();
				String name = f.getName().split("\\.")[0];
				jbtn_se.setText(name);
				jta.append("주는 사람이 " + name + "(으)로 선택되었습니다.\n");
				keyName.put(walletS.publicKey, name);
			} catch (Exception e) {
				jta.append("주는 사람 선택 안 함\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			jta.append("주는 사람 선택 실패\n");
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		reFreshBalance();
	}

	// 받는 사람 선택
	private void selectMiner() {
		jta.append("채굴하는 사람 선택하기...\n");
		try {
			jfc_p.showOpenDialog(null);
			File f = jfc_p.getSelectedFile();
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
				publicKeyM = (PublicKey) ois.readObject();
				String name = f.getName().split("\\.")[0];
				jbtn_ms.setText(name);
				jta.append("채굴하는 사람이 " + name + "(으)로 선택되었습니다.\n");
				keyName.put(publicKeyM, name);
			} catch (Exception e) {
				jta.append("채굴하는 사람 선택 안 함\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			jta.append("채굴하는 사람 선택 실패\n");
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		reFreshBalance();
	}

	// 받는 사람 선택
	private void selectRecipient() {
		jta.append("받는 사람 선택하기...\n");
		try {
			jfc_p.showOpenDialog(null);
			File f = jfc_p.getSelectedFile();
			try {
				ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(f)));
				// walletR = (Wallet) ois.readObject();
				publicKeyR = (PublicKey) ois.readObject();
				String name = f.getName().split("\\.")[0];
				jbtn_re.setText(name);
				jta.append("받는 사람이 " + name + "(으)로 선택되었습니다.\n");
				// keyName.put(walletR.publicKey, name);
				keyName.put(publicKeyR, name);
			} catch (Exception e) {
				jta.append("받는 사람 선택 안 함\n");
				e.printStackTrace();
			}
		} catch (Exception e) {
			jta.append("받는 사람 선택 실패\n");
			e.printStackTrace();
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		reFreshBalance();
	}

	// 송금 트랜잭션 생성
	private void doTransaction() {
		jta.append("거래하기...\n");
		if (walletS == null) {
			jta.append("주는 사람을 선택하세요.");
			// } else if (walletR == null) {
		} else if (publicKeyR == null) {
			jta.append("받는 사람을 선택하세요.");
		} else {
			try {
				BigDecimal value = new BigDecimal(jtf.getText());
				Transaction tx = ChainUtil.sendFunds(walletS.publicKey, publicKeyR, value);
				tx.senderName = keyName.get(walletS.publicKey);
				tx.recipientName = keyName.get(publicKeyR);
				// 유효한 트랜잭션일 경우 미처리목록에 추가
				tx = tx.generateSignature(walletS.privateKey);
				if (ChainUtil.isTransactionValid(tx)) {

					// 새로 추가되는 트랜잭션을 다른 노드들에 전달
					broadCastAll(BcP2PVo.NEW_TRANSACTION, tx);
				}
			} catch (NumberFormatException e) {
				jta.append("금액칸에는 숫자만 입력하세요.\n");
			} catch (Exception e) {
				jta.append("송금 실패 로그창 확인\n");
				e.printStackTrace();
			}
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(jbtn_bc)) {
			createBlock();
		} else if (o.equals(jbtn_ms)) {
			selectMiner();
		} else if (o.equals(jbtn_am)) {
			autoMining();
		} else if (o.equals(jbtn_ps)) {
			savePublicKey();
		} else if (o.equals(jbtn_re)) {
			selectRecipient();
		} else if (o.equals(jbtn_wc)) {
			createWallet();
		} else if (o.equals(jbtn_se)) {
			selectSender();
		} else if (o.equals(jbtn_tx)) {
			doTransaction();
		} else {
			logger.info(e.getSource().toString());
		}
	}

	/*
	 * < 체인 화면 기능 코드
	 *
	 * 체인 네트워크 코드 >
	 */

	// 정해진 서버에 접속해서 네트워크에 접속하는 노드들의 ip주소를 받는다
	private void connectServer() {
		try {
			String networkIp = JOptionPane.showInputDialog("Input server ip");
			csTCP.connect(new InetSocketAddress(networkIp, NETWORK_PORT));// Time Out Point
			jta.append("블록체인 네트워크 접속: " + networkIp + ":" + NETWORK_PORT + "\n");
			logger.info("Chain connectServer Start, Port: " + NETWORK_PORT);
			new Thread(new BcNodeThread(this, csTCP)).start();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	// 네트워크에 접속하는 노드들과 연결
	protected void openPeer(String myIp) {
		logger.info("open peer(" + myIp + ")");
		try {
			// 새로 네트워크에 접속시 네트워크에 접속해있는 모든 노드에게 자신의 ip를 전송하고,
			// ip를 전달받은 노드는 이 노드에 연결을 요청하게 됨.
			// 그 때 연결을 수락하고 대상과 통신하는 새로운 쓰레드를 생성
			ssTCP = new ServerSocket();
			ssTCP.bind(new InetSocketAddress(myIp, P2P_PORT));
			logger.info("ssTCP: " + ssTCP.getLocalSocketAddress());
			for (;;) {
				p2pTCP = ssTCP.accept();// Blocking Point
				new Thread(new BcNodeP2PThread(this, p2pTCP)).start();
				jta.append("새로운 노드가 연결되었습니다.: " + p2pTCP.getInetAddress() + ":" + P2P_PORT + "\n");
			}
		} catch (BindException e) {
			Thread.interrupted();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<String> getHashList() {
		List<String> hashList = new ArrayList<>();
		for (Block b : chain) {
			hashList.add(b.hash);
		}
		return hashList;
	}

	public void addNameForKey(KeyName kn) {
		keyName.put(kn.getMyKey(), kn.getName());
		try {
			File f = new File(publickey_path + kn.getName() + ".public_key");
			oos = new ObjectOutputStream(new FileOutputStream(f));
			oos.writeObject(kn.getMyKey());
			jta.append("공개키 저장 완료: " + kn.getName() + "\n");
		} catch (Exception e) {
			jta.append("공개키 저장 실패\n");
			e.printStackTrace();
		} finally {
			try {
				if (oos != null)
					oos.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	protected String analysisRequest(BcP2PDto dto) {
		logger.info("analysisRequest(" + dto.getHeader() + ")");
		try {
			if (BcP2PVo.NEW_BLOCK.equals(dto.getHeader())) {
				return analysisNewBlock((Block) dto.getData());
			}
			if (BcP2PVo.NEW_TRANSACTION.equals(dto.getHeader())) {
				return analysisNewTransaction((Transaction) dto.getData());
			}
			if (BcP2PVo.GIVE_ME_BLOCK_LIST.equals(dto.getHeader())) {
				return BcP2PVo.BLOCK_LIST;
			}
			if (BcP2PVo.BLOCK_LIST.equals(dto.getHeader())) {
				try {
					return analysisBlockList((ArrayList<String>) dto.getData());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (BcP2PVo.KEY_NAME.equals(dto.getHeader())) {
				addNameForKey((KeyName) dto.getData());
				return "";
			}
			//			if (BcP2PVo.SHOW_ME_CURRENT_CHAIN_LENGTH.equals(dto.getHeader())) {
			//				return BcP2PVo.CHAIN_LENGTH;
			//			}
			//			if (BcP2PVo.CHAIN_LENGTH.equals(dto.getHeader())) {
			//				// 처음 노드를 구동했을 때 다른 노드들에게서 노드를 받는 부분
			//				// 생략. 다른 노드가 새로운 블록을 채굴했을 때
			//				// analysisNewBlock를 통해 최신 블록들을 받는다.
			//				return "";
			//			}
			// 존재하지 않는 헤더를 전송받았을 경우 해당 노드와의 연결을 끊는다.( or return "")
			return BcP2PVo.EVIL_DETECTION;
		} catch (Exception e) {
			e.printStackTrace();
			// 예외 발생시 해당 노드와의 연결을 끊는다.( or return "")
			return BcP2PVo.EVIL_DETECTION;
		}
	}

	private String analysisNewBlock(Block newBlock) {
		jta.append("새로운 블록을 전송받았습니다. 블록 번호: " + newBlock.chainCount + "\n");
		logger.info("analysisNewBlock(" + newBlock.chainCount + ")");
		// 블록의 위치에 비해 체인의 길이가 짧거나 이어지는 블록의 해시가 서로 일치하지 않을 경우
		// 자신의 체인에서 블록들의 해시를 리스트로 만들어 전송후 필요한 만큼 블록 리스트를 받는다
		if (chain.size() < newBlock.chainCount
				|| chain.size() > 0 && !chain.get(chain.size() - 1).hash.equals(newBlock.previousHash)) {
			return BcP2PVo.GIVE_ME_BLOCK_LIST;
		}
		// 잘못된 블록을 전송받았을 경우 해당 노드와의 연결을 끊는다.
		if (!chainingBlock(newBlock, true, true)) {
			return BcP2PVo.EVIL_DETECTION;
		}
		return "";
	}

	private String analysisNewTransaction(Transaction newTx) {
		jta.append("새로운 트랜잭션을 전송받았습니다. 트랜잭션 밸류: " + newTx.value + "\n");
		ChainUtil.isTransactionValid(newTx);
		return "";
	}

	private String analysisBlockList(ArrayList<String> bsList) {
		jta.append("새로운 블록목록을 전송받았습니다.\n");
		List<Block> bList = new ArrayList<>();
		ObjectInputStream tempOis;
		try {
			for (String bs : bsList) {
				tempOis = new ObjectInputStream(
						new BufferedInputStream(new ByteArrayInputStream(bs.getBytes("ISO-8859-1"))));
				bList.add((Block) tempOis.readObject());
				tempOis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return BcP2PVo.EVIL_DETECTION;
		}
		// 블록리스트의 체인길이가 짧을 경우
		if (bList.get(bList.size() - 1).chainCount < chain.size()) {
			return "";
		}
		// 블록리스트의 체인길이가 길 경우
		if (bList.get(bList.size() - 1).chainCount > chain.size()) {
			removeBlock(bList.get(0).chainCount, false);
		}
		for (Block b : bList) {
			if (chain.size() < b.chainCount || chain.size() == 0
					|| chain.get(chain.size() - 1).hash.equals(b.previousHash)) {
				if (!chainingBlock(b, true, true)) {
					removeBlock(bList.get(0).chainCount, false);
					loadBlocks(bList.get(0).chainCount);
					return BcP2PVo.EVIL_DETECTION;
				}
			}
		}
		return "";
	}

	protected ArrayList<String> analysisHashList(List<String> hList) {
		logger.info("analysisHashList(hList)");
		List<String> myList = getHashList();
		if (myList.size() <= hList.size()) {
			return null;
		}
		int i = 0;
		for (; i < hList.size(); i++) {
			if (!myList.get(i).equals(hList.get(i))) {
				break;
			}
		}
		ArrayList<String> bList = new ArrayList<>();
		try {
			ByteArrayOutputStream tbaos;
			ObjectOutputStream toos;
			for (; i < myList.size(); i++) {
				tbaos = new ByteArrayOutputStream();
				toos = new ObjectOutputStream(new BufferedOutputStream(tbaos));
				toos.writeObject(chain.get(i));
				toos.close();
				String b = new String(tbaos.toByteArray(), "ISO-8859-1");
				bList.add(b);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return bList;
	}

	public static void broadCastAll(String header, Object data) {
		logger.info("broadCastAll(" + header + ") to " + peerList.size());
		BcP2PDto dto = new BcP2PDto(header, data);
		try {
			for (BcNodeP2PThread p : peerList) {
				p.broadCast(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/* < 체인 네트워크 코드 */
}
