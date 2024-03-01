package zerobase.weather;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class JdbcMemoRepositoryTest {

//    @Autowired
//    JdbcMemoRepository jdbcMemoRepository;
//
//    @Test
//    void insertMemoTest() {
//        //given
//        Memo newMemo = new Memo(2, "this is new memo");
//
//        //when
//        jdbcMemoRepository.save(newMemo);
//
//        //then
//        Optional<Memo> result = jdbcMemoRepository.findById(2);
//        assertEquals("this is new memo", result.get().getText());
//
//    }
//
//    @Test
//    void findAllMemoTest() {
//        List<Memo> memoList = jdbcMemoRepository.findAll();
//        System.out.println(memoList);
//        assertNotNull(memoList);
//    }

}
