/*
 * <summary></summary>
 * <author>Hankcs</author>
 * <email>me@hankcs.com</email>
 * <create-date>2016-07-23 PM4:24</create-date>
 *
 * <copyright file="WordVectorMap.java" company="码农场">
 * Copyright (c) 2008-2016, 码农场. All Right Reserved, http://www.hankcs.com/
 * This source is subject to Hankcs. Please contact Hankcs to get more information.
 * </copyright>
 */
package demo.hankcs.hanlp.mining.word2vec;

import java.io.IOException;
import java.util.*;

/**
 * 词向量模型
 *
 * @author hankcs
 */
public class WordVectorModel extends AbstractVectorModel<String>
{
    /**
     * 加载模型<br>
     *
     * @param modelFileName 模型路径
     * @throws IOException 加载错误
     */
    public WordVectorModel(String modelFileName) throws IOException
    {
        this(modelFileName, new TreeMap<String, Vector>());
    }

    /**
     * 加载模型
     *
     * @param modelFileName 模型路径
     * @param storage 一个空白的Map（HashMap等）
     * @throws IOException 加载错误
     */
    public WordVectorModel(String modelFileName, Map<String, Vector> storage) throws IOException
    {
        super(loadVectorMap(modelFileName, storage));
    }

    private static Map<String, Vector> loadVectorMap(String modelFileName, Map<String, Vector> storage) throws IOException
    {
        VectorsReader reader = new VectorsReader(modelFileName);
        reader.readVectorFile();
        for (int i = 0; i < reader.vocab.length; i++)
        {
            storage.put(reader.vocab[i], new Vector(reader.matrix[i]));
        }
        return storage;
    }

    /**
     * 返回跟 A - B + C 最相似的词语,比如 中国 - 北京 + 东京 = 日本。输入顺序按照 中国 北京 东京
     *
     * @param A 做加法的词语
     * @param B 做减法的词语
     * @param C 做加法的词语
     * @return 与(A - B + C)语义距离最近的词语及其相似度列表
     */
    public List<Map.Entry<String, Float>> analogy(String A, String B, String C)
    {
        return analogy(A, B, C, 10);
    }

    /**
     * 返回跟 A - B + C 最相似的词语,比如 中国 - 北京 + 东京 = 日本。输入顺序按照 中国 北京 东京
     *
     * @param A    做加法的词语
     * @param B    做减法的词语
     * @param C    做加法的词语
     * @param size topN个
     * @return 与(A - B + C)语义距离最近的词语及其相似度列表
     */
    public List<Map.Entry<String, Float>> analogy(String A, String B, String C, int size)
    {
        Vector a = storage.get(A);
        Vector b = storage.get(B);
        Vector c = storage.get(C);
        if (a == null || b == null || c == null)
        {
            return Collections.emptyList();
        }

        List<Map.Entry<String, Float>> resultList = nearest(a.minus(b).add(c), size + 3);
        ListIterator<Map.Entry<String, Float>> listIterator = resultList.listIterator();
        while (listIterator.hasNext())
        {
            String key = listIterator.next().getKey();
            if (key.equals(A) || key.equals(B) || key.equals(C))
            {
                listIterator.remove();
            }
        }

        if (resultList.size() > size)
        {
            resultList = resultList.subList(0, size);
        }

        return resultList;
    }

    @Override
    public Vector query(String query)
    {
        return vector(query);
    }
}