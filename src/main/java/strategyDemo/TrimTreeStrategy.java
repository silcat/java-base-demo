package strategyDemo;

/**
 * 字典树
 */
public class TrimTreeStrategy {
    TrieNode root;

    public TrimTreeStrategy() {
        this.root = new TrieNode();
    }
    public void put(String word){
        if (word == null){
            return;
        }
        char[] chars = word.toCharArray();
        TrieNode node = root;
        int index = 0;
        for (int i = 0;i< chars.length;i++){
            index = chars[i] - 'a';
            if (node.nexts[index] == null) {
                node.nexts[index] = new TrieNode();
            }
            TrieNode trieNode = node.nexts[index];
            trieNode.path ++;
            node = trieNode;
        }
        node.end++;

    }
    public TrieNode get(String word){
        if (word == null){
            return null;
        }
        char[] chars = word.toCharArray();
        TrieNode node = root;
        int index = 0;
        for (char c : chars) {
            index = c - 'a';
            if (node.nexts[index] == null) {
                return null;
            }
            node = node.nexts[index];
        }
        return node;
    }

    class TrieNode {
        /**
         * path表示字符路过这个结点的次数（即表示存在以当前结点为前缀的字符有多少个）；
         * end记录以当前结点为结束的字符有多少个。
         */
        public int path;
        public int end;
        public TrieNode[] nexts;

        public TrieNode() {
            path = 0;
            end = 0;
            //只能存英文小写字母，如果是ASCII码可以生成256大小的数组
            //如果想存更多种类的字符可以改为map结构
            nexts = new TrieNode[26];
        }
    }


    public static void main(String[] args) {
        TrimTreeStrategy trieTree= new TrimTreeStrategy();
        trieTree.put("hours");
        trieTree.put("hour");
        trieTree.put("honer");
        TrieNode trieNode = trieTree.get("ho");
    }


}
