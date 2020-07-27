package blockchain.node;

public class BcP2PVo {
	public static final String SHARP = "\r\n";// Separator
	public static final String EVIL_DETECTION = "ED";// 잘못된 정보를 퍼뜨리는 노드 발견
	public static final String NEW_BLOCK = "NB";// 새로운 블록을 채굴했을 경우
	public static final String NEW_TRANSACTION = "NT";// 새로운 트랜잭션이 추가되었을 경우
	public static final String GIVE_ME_BLOCK_LIST = "GMBL";// 블록 리스트를 요청, 요청시 자신의 블록의 해시 리스트를 전달
	public static final String BLOCK_LIST = "BL";// 요청한 블록 리스트
//	public static final String SHOW_ME_CURRENT_CHAIN_LENGTH = "SMCCL";// 처음 네트워크 접속시 모든 노드에게 현재 체인 길이를 요청
//	public static final String CHAIN_LENGTH = "CL";// 체인 길이
	public static final String KEY_NAME = "KN";// 키 + 네임
}
