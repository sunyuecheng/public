package com.sct.hibernatetest.dao;

import com.sct.hibernatetest.entity.UserInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IUserInfoJpaDao extends JpaRepository<UserInfo,String>, PagingAndSortingRepository<UserInfo,String> ,JpaSpecificationExecutor<UserInfo> {

    public void deleteById(String id);
    public UserInfo findById(String id);

    @Modifying
    @Query("update UserInfo u set u.name=?1 where u.id=?2")
    public int updateUserInfo(String name,String id);

    @Query("select u from UserInfo u where u.lastViewDate>=?1 and u.lastViewDate<=?2")
    public List<UserInfo> getUserInfoListByLastViewDate(Long beginDate, Long endDate, Pageable pageable);

//    @Query(nativeQuery = true, value = "select u from t_user_info u where u.last_view_date>=?1 and u.lastViewDate<=?2")
//    public List<UserInfo> getUserInfoListByLastViewDate(Long beginDate, Long endDate, Pageable pageable);

}
