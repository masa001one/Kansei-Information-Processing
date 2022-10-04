import java.util.*;
import java.io.*;

/**
 * ルールベースを表すクラス．
 *
 * 
 */
class RuleBase {
    ArrayList<String> wm;
    ArrayList<Rule> rules;

    RuleBase(ArrayList<Rule> theRules, ArrayList<String> theWm) {
        wm = theWm;
        rules = theRules;
    }

    public void setWm(ArrayList<String> theWm) {
        wm = theWm;
    }

    public void setRules(ArrayList<Rule> theRules) {
        rules = theRules;
    }

    /**
     * 前向き推論を行うためのメソッド
     *
     */
    public void forwardChain() {
        boolean newAssertionCreated;
        // 新しいアサーションが生成されなくなるまで続ける．
        do {
            newAssertionCreated = false;
            for (int i = 0; i < rules.size(); i++) {
                Rule aRule = rules.get(i); // rulesの着目要素
                System.out.println("apply rule:" + aRule.getName()); // 着目ルールの名前
                ArrayList<String> antecedents = aRule.getAntecedents(); // 着目ルールの前件
                String consequent = aRule.getConsequent(); // 着目ルールの後件
                ArrayList<HashMap<String, String>> bindings = matchingAssertions(antecedents);
                if (bindings != null) {
                    for (int j = 0; j < bindings.size(); j++) {
                        // 後件をインスタンシエーション
                        String newAssertion = instantiate(consequent, bindings.get(j));
                        // ワーキングメモリーになければ成功
                        if (!wm.contains(newAssertion)) {
                            System.out.println("Success: " + newAssertion);
                            wm.add(newAssertion);
                            newAssertionCreated = true;
                        }
                    }
                }
            }
            System.out.println("Working Memory" + wm);
        } while (newAssertionCreated);
        System.out.println("No rule produces a new assertion");
    }

    /**
     * マッチするアサーションに対するバインディング情報を返す
     * （再帰的）
     *
     * @param 前件を示す ArrayList
     * @return バインディング情報が入っている ArrayList
     */
    public ArrayList<HashMap<String, String>> matchingAssertions(ArrayList<String> theAntecedents) {

        ArrayList<HashMap<String, String>> bindings = new ArrayList<>();
        return matchable(theAntecedents, 0, bindings);
    }

    private ArrayList<HashMap<String, String>> matchable(ArrayList<String> theAntecedents, int n,
            ArrayList<HashMap<String, String>> bindings) {

        if (n == theAntecedents.size()) {
            return bindings;
        } else if (n == 0) {
            boolean success = false;
            for (int i = 0; i < wm.size(); i++) {
                HashMap<String, String> binding = new HashMap<String, String>();
                if ((new Matcher()).matching(theAntecedents.get(n),
                        wm.get(i), binding)) {
                    bindings.add(binding);
                    success = true;
                }
            }
            if (success) {
                return matchable(theAntecedents, n + 1, bindings);
            } else {
                return null;
            }
        } else {
            boolean success = false;
            ArrayList<HashMap<String, String>> newBindings = new ArrayList<HashMap<String, String>>();
            for (int i = 0; i < bindings.size(); i++) {
                for (int j = 0; j < wm.size(); j++) {
                    if ((new Matcher()).matching(
                            theAntecedents.get(n),
                            wm.get(j),
                            bindings.get(i))) {
                        newBindings.add(bindings.get(i));
                        success = true;
                    }
                }
            }
            if (success) {
                return matchable(theAntecedents, n + 1, newBindings);
            } else {
                return null;
            }
        }
    }

    private String instantiate(String thePattern,
            HashMap<String, String> theBindings) {

        String result = new String();
        StringTokenizer st = new StringTokenizer(thePattern);

        for (int i = 0; i < st.countTokens();) {
            String token = st.nextToken();
            if (var(token)) {
                result = result + " " + theBindings.get(token);
            } else {
                result = result + " " + token;
            }
        }
        return result.trim();
    }

    private boolean var(String str1) {
        // 先頭が ? なら変数
        return str1.startsWith("?");
    }

}
