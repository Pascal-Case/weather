package zerobase.weather;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional // 테스트코드에 Transactional 어노테이션시 commit 안함
public class JpaMemoRepositoryTest {

//    @Autowired
//    JpaMemoRepository jpaMemoRepository;
//
//    @Test
//    void insertMemoTest() {
//        //given
//        Memo newMemo = new Memo(10, "memo1");
//        //when
//        jpaMemoRepository.save(newMemo);
//
//        //then
//        List<Memo> memoList = jpaMemoRepository.findAll();
//        assertFalse(memoList.isEmpty());
//
//    }
//
//    @Test
//    void findByIdTest() {
//        //given
//        Memo newMemo = new Memo(11, "aaa");
//        //when
//        Memo memo = jpaMemoRepository.save(newMemo);
//        //then
//        Optional<Memo> result = jpaMemoRepository.findById(memo.getId());
//        assertEquals("aaa", result.get().getText());
//    }
}
