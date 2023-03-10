package com.example.week3_spring.service;

import com.example.week3_spring.domain.Article;
import com.example.week3_spring.domain.ArticleReqDto;
import com.example.week3_spring.domain.ArticleResDto;
import com.example.week3_spring.exception.CustomException;
import com.example.week3_spring.repository.ArticleRepository;
import org.apache.ibatis.jdbc.Null;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.InstanceOfAssertFactories.OPTIONAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ArticleServiceTest {

    @Mock
    private ArticleRepository articleRepository;

    @InjectMocks
    private ArticleService  articleService;

//    @Autowired
//    private ArticleService articleService;

    @BeforeEach
    void beforeEach() {
        articleRepository.deleteAll();
    }

    @Test
    @DisplayName("전체 조회")
    void findAll() {
        //given

        //stub
        List<Article> articleList = new ArrayList<>();
        articleList.add(Article.builder()
                .id(1L)
                .date("2023/02/01")
                .showCount(12L)
                .clickCount(13L)
                .adMoney(15L)
                .soldMoney(16L)
                .soldCount(17L)
                .build());
        articleList.add(Article.builder()
                .id(2L)
                .date("2023/02/02")
                .showCount(112L)
                .clickCount(113L)
                .adMoney(115L)
                .soldMoney(116L)
                .soldCount(117L)
                .build());

        when(articleRepository.findAll()).thenReturn(articleList);

        //when
        List<Article> article = articleService.findAll();

        //then
        assertThat(article.get(0).getId()).isEqualTo(1L);
        assertThat(article.get(1).getId()).isEqualTo(2L);
    }

    @Test
    @DisplayName("단일_조회_성공")
    void findById() {
        //given
        Long id = 1L; //파라미터

        Article article = Article.builder()
                .id(1L)
                .date("2023/02/02")
                .showCount(112L)
                .clickCount(113L)
                .adMoney(115L)
                .soldMoney(116L)
                .soldCount(117L)
                .build();
        Optional<Article> ArticleOP = Optional.of(article);

        //stub
        when(articleRepository.findById(1L)).thenReturn(ArticleOP);

        //when
        ArticleOP.get().ArticleToarticleResDto();
        ArticleResDto dto = articleService.findById(id);

        //then
        assertThat(dto.getDate()).isEqualTo("2023/02/02");
    }

    @Test
    @DisplayName("단일_조회_예외처리")
    void findById_Exception() throws Exception{
        //given
        Long id = 1L; //파라미터


        //stub
        when(articleRepository.findById(1L)).thenReturn(Optional.empty());

        //when


        //then
        Assertions.assertThrows(CustomException.class, () -> {
            articleService.findById(id); //when
        });

    }

    @Test
    @DisplayName("저장 테스트")
    void savaArticle() {
        //given
        Long id = 1L;

        ArticleReqDto articleReqDto = new ArticleReqDto();
        articleReqDto.setDate("2023/02/02");
        articleReqDto.setShowCount(11L);
        articleReqDto.setClickCount(11L);
        articleReqDto.setAdMoney(11L);
        articleReqDto.setSoldMoney(11L);
        articleReqDto.setSoldCount(11L);

        Article article = Article.builder()
                .id(1L)
                .date("2023/02/02")
                .showCount(112L)
                .clickCount(113L)
                .adMoney(115L)
                .soldMoney(116L)
                .soldCount(117L)
                .build();

        //stub
        when(articleRepository.save(any())).thenReturn(article);

        //when
        Long result = articleService.savaArticle(articleReqDto);

        //then
        assertThat(result).isEqualTo(1L);
        //entity나 dto에 있는 값들을 검증이 힘듬.
    }

    @Test
    @DisplayName("수정_테스트_성공")
    void updateArticle_SUCESS() {
        //given
        Long id = 1L; //파라미터로 받음

        ArticleReqDto articleReqDto = new ArticleReqDto(); //파라미터로 받음
        articleReqDto.setDate("2023/02/05");
        articleReqDto.setShowCount(11L);
        articleReqDto.setClickCount(11L);
        articleReqDto.setAdMoney(11L);
        articleReqDto.setSoldMoney(11L);
        articleReqDto.setSoldCount(11L);

        Article givenArticle = Article.builder()
                .id(1L)
                .date("2023/02/02")
                .showCount(112L)
                .clickCount(113L)
                .adMoney(115L)
                .soldMoney(116L)
                .soldCount(117L)
                .build();
        givenArticle.makeswitchCount();


        //stub
        Optional<Article> articleOP = Optional.of(givenArticle);
        when(articleRepository.findById(id)).thenReturn(articleOP);

        //when
        Long afterId = articleService.updateArticle(id,articleReqDto);

        //then
        assertThat(afterId).isEqualTo(1L);

    }
    @Test
    @DisplayName("수정_테스트_예외처리_아이디값_없음")
    void updateArticle_NULL_ID() {
        //given
        Long id = 1L; //파라미터로 받음

        ArticleReqDto articleReqDto = new ArticleReqDto(); //파라미터로 받음

        Article article = new Article();
        Optional<Article> a = Optional.of(article);

        //stub
        when(articleRepository.findById(id)).thenReturn(Optional.empty());

        //when

        //then
        Assertions.assertThrows(CustomException.class, () -> {
            articleService.findById(id); //when
        });

    }

    @Test
    @DisplayName("삭제_테스트_성공")
    void deleteById_SUCCESS() {
        //given

        //stub
        when(articleRepository.existsById(any())).thenReturn(true);

        //when
        articleService.deleteById(21L);

        //then
        verify(articleRepository,atLeastOnce()).deleteById(21L);
    }

    @Test
    @DisplayName("삭제_테스트_예외처리_아이디_존재_안함")
    void deleteById_NULL_ID() {
        //given

        //stub
        when(articleRepository.existsById(any())).thenReturn(false);

        //when


        //then
        Assertions.assertThrows(CustomException.class, () -> {
            articleService.deleteById(any()); //when
        });
    }
}