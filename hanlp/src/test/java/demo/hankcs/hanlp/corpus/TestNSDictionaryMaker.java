package demo.hankcs.hanlp.corpus;

import demo.hankcs.hanlp.corpus.dictionary.EasyDictionary;
import demo.hankcs.hanlp.corpus.dictionary.NSDictionaryMaker;
import demo.hankcs.hanlp.corpus.document.CorpusLoader;
import demo.hankcs.hanlp.corpus.document.Document;

public class TestNSDictionaryMaker {

    public static void main(String[] args)
    {
        EasyDictionary dictionary = EasyDictionary.create("data/dictionary/2014_dictionary.txt");
        final NSDictionaryMaker nsDictionaryMaker = new NSDictionaryMaker(dictionary);
        CorpusLoader.walk("D:\\JavaProjects\\CorpusToolBox\\data\\2014\\", new CorpusLoader.Handler()
        {
            @Override
            public void handle(Document document)
            {
                nsDictionaryMaker.compute(document.getComplexSentenceList());
            }
        });
        nsDictionaryMaker.saveTxtTo("D:\\JavaProjects\\HanLP\\data\\test\\place\\ns");
    }
}
