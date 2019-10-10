package com.ipcoding.gitignore;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author nokdoot
 */

public class TestGitignore {

    @Test
    public void test1() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        jsonPatternArray.put("**/logs");

        jsonPathArray.put("logs/debug.log");
        jsonPathArray.put("logs/monday/foo.bar");
        jsonPathArray.put("build/logs/debug.log");

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs/debug.log",
                "logs/monday/foo.bar",
                "build/logs/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test2() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        jsonPatternArray.put("**/logs/debug.log");

        jsonPathArray.put("logs/debug.log");
        jsonPathArray.put("build/logs/debug.log");
        jsonPathArray.put("logs/build/debug.log");

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs/debug.log",
                "build/logs/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test3() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        jsonPatternArray.put("*.log");

        jsonPathArray.put("debug.log");
        jsonPathArray.put("foo.log");
        jsonPathArray.put(".log");
        jsonPathArray.put("logs/debug.log");

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug.log",
                "foo.log",
                ".log",
                "logs/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test4() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        jsonPatternArray.put("*.log");
        jsonPatternArray.put("!important.log");

        jsonPathArray.put("debug.log");
        jsonPathArray.put("trace.log");
        jsonPathArray.put("important.log");
        jsonPathArray.put("logs/important.log");

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug.log",
                "trace.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test5() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "*.log",
                "!important/*.log",
                "trace.*"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debug.log",
                "important/trace.log",
                "important/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug.log",
                "important/trace.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test6() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "/debug.log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debug.log",
                "logs/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test7() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "debug.log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debug.log",
                "logs/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug.log",
                "logs/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test8() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "debug?.log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debug0.log",
                "debugg.log",
                "debug10.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug0.log",
                "debugg.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test9() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "debug[0-9].log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debug0.log",
                "debug1.log",
                "debug10.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug0.log",
                "debug1.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test10() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "debug[01].log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debug0.log",
                "debug1.log",
                "debug2.log",
                "debug01.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug0.log",
                "debug1.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test11() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "debug[!01].log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debug2.log",
                "debug0.log",
                "debug1.log",
                "debug01.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debug2.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test12() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "debug[a-z].log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "debuga.log",
                "debugb.log",
                "debug1.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "debuga.log",
                "debugb.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test13() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "logs"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "logs",
                "logs/debug.log",
                "logs/latest/foo.bar",
                "build/logs",
                "build/logs/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs",
                "logs/debug.log",
                "logs/latest/foo.bar",
                "build/logs",
                "build/logs/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test14() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "logs/"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "logs/debug.log",
                "logs/latest/foo.bar",
                "build/logs/foo.bar",
                "build/logs/latest/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs/debug.log",
                "logs/latest/foo.bar",
                "build/logs/foo.bar",
                "build/logs/latest/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test15() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "logs/",
                "!logs/important.log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "logs/debug.log",
                "logs/important.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs/debug.log",
                "logs/important.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test16() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "logs/**/debug.log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "logs/debug.log",
                "logs/monday/debug.log",
                "logs/monday/pm/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs/debug.log",
                "logs/monday/debug.log",
                "logs/monday/pm/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test17() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "logs/*day/debug.log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "logs/monday/debug.log",
                "logs/tuesday/debug.log",
                "logs/latest/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs/monday/debug.log",
                "logs/tuesday/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test18() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "logs/debug.log"
        });

        makeJsonArray(jsonPathArray, new String[] {
                "logs/debug.log",
                "debug.log",
                "build/logs/debug.log"
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "logs/debug.log"
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test19() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "log",
        });

        makeJsonArray(jsonPathArray, new String[] {
                "log/debug.log",
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
                "log/debug.log",
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    @Test
    public void test20() {
        JSONObject jsonObject = new JSONObject();
        JSONArray jsonPathArray = new JSONArray();
        JSONArray jsonPatternArray = new JSONArray();

        makeJsonArray(jsonPatternArray, new String[] {
                "debug",
        });

        makeJsonArray(jsonPathArray, new String[] {
                "log/debug.log",
        });

        jsonObject.put("patterns", jsonPatternArray);
        jsonObject.put("paths", jsonPathArray);

        String[] expected = new String[]{
        };

        Assert.assertTrue(gitignore(jsonObject, expected));
    }

    private void makeJsonArray(JSONArray jsonArray, String[] strings) {
        for (String str : strings) {
            jsonArray.put(str);
        }
    }

    private boolean gitignore(JSONObject jsonObject, String[] expected) {

        JSONArray paths = jsonObject.getJSONArray("paths");
        JSONArray patterns = jsonObject.getJSONArray("patterns");

        boolean[] ignoredPaths = new boolean[paths.length()];

        LOOP:
        for (int i = 0; i < patterns.length(); i++) {
            String pattern1 = patterns.get(i).toString();
            String regex = makeRegex(pattern1);

            if (includingWith(regex)) {
                for (int j = 0; j < paths.length(); j++) {
                    String path = paths.get(j).toString().trim();
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(path);
                    if (m.find()) { ignoredPaths[j] = true; }
                }
            }
            else {
                for (int j = 0; j < patterns.length(); j++) {
                    String pattern2 = patterns.get(j).toString().trim();
                    if (pattern1.equals(pattern2)) { break; }
                    pattern1 = pattern1.replaceFirst("^!", "");
                    if (relationsOfSuffix(pattern1, pattern2)) { continue LOOP; }
                }
                regex = regex.replaceFirst("^!", "");
                for (int j = 0; j < paths.length(); j++) {
                    String path = paths.get(j).toString().trim();
                    Pattern p = Pattern.compile(regex);
                    Matcher m = p.matcher(path);
                    if (m.find()) { ignoredPaths[j] = false; }
                };
            }
        }

        ArrayList<String> result = new ArrayList<>();


        int n = paths.length();
        for (int i = 0; i < n; i++) {
            if (ignoredPaths[i]) {
                result.add(paths.get(i).toString());
            }
        }

        if (result.size() != expected.length) { return false; }

        int m = result.size();

        for (int i = 0; i < m; i++) {
            if (!expected[i].equals(result.get(i))) { return false; }
        }

        return true;
    }

    private boolean relationsOfSuffix(String pattern1, String pattern2) {
        String suffix = "";
        Pattern p = Pattern.compile("(\\w+/?)$");
        Matcher m = p.matcher(pattern1);
        if (m.find()) {
            suffix = m.group(1);
        }
        return Pattern.compile("^" + suffix).matcher(pattern2).find();
    }

    private boolean includingWith(String regex) {
        if (regex.startsWith("!")) { return false; }
        else { return true; }
    }

    private String makeRegex(String pattern) {

        // comment
        if (Pattern.compile("^#").matcher(pattern).find()) { return ""; }

        // only .
        if (Pattern.compile("^\\.+$").matcher(pattern).find()) { return ""; }

        // . -> \.
        pattern = pattern.replaceAll("\\.", "\\\\.");

        // ***... -> **
        pattern = pattern.replaceAll("\\*{3,}", "\\*\\*");

        // ** -> dump string
        pattern = pattern.replaceAll("\\*\\*", "xxxxx0xxxxx");

        // * -> .*
        pattern = pattern.replaceAll("\\*", "\\.*");

        // dump string -> **
        pattern = pattern.replaceAll("xxxxx0xxxxx", "\\*\\*");

        // ^**/ -> .*
        pattern = pattern.replaceAll("^\\*\\*/", "\\.\\*");

        // ? -> .?
        pattern = pattern.replaceAll("\\?", "\\.\\?");

        // ** -> .*
        pattern = pattern.replaceAll("\\*\\*", "\\.\\*");

        // ^/ -> start with ^
        pattern = pattern.replaceAll("^/", "^");

        // [!...] -> [^...]
        Pattern p = Pattern.compile("(\\[!(.*)])");
        Matcher m = p.matcher(pattern);
        if (m.find()) { pattern = pattern.replaceAll(m.group(1), "[^"+m.group(2)+"]"); }

        // abc/.*/abc -> /.*
        pattern = pattern.replaceAll("/\\.\\*/", "/\\.\\*");

        // only words
        p = Pattern.compile("^(\\w*(\\\\.)*\\w+)$");
        m = p.matcher(pattern);
        if (m.find()) { pattern = "([\\/]|^)" + m.group(1) + "((?=\\/|$))"; }

        if (
            Pattern.compile("/").matcher(pattern).find()                // slash at beginning or middle
            && !Pattern.compile("/$").matcher(pattern).find()   // slash at not end
            && !Pattern.compile("^!").matcher(pattern).find()   // not start with !
            && !Pattern.compile("\\\\b").matcher(pattern).find()  // not words only
            && !Pattern.compile("^\\(").matcher(pattern).find() // not start with (, "ONLYWORDS"
        ) {
            pattern = "^" + pattern;
        }

        return pattern;
    }
}
