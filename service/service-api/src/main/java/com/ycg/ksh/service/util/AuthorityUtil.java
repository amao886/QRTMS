package com.ycg.ksh.service.util;/**
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/19 0019
 */

import com.ycg.ksh.common.util.StringUtils;
import com.ycg.ksh.constant.CoreConstants;
import com.ycg.ksh.entity.persistent.SysMenu;
import com.ycg.ksh.entity.service.AuthorityMenu;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @TODO 类描述
 * <p>
 * @developer Create by <a href="mailto:110686@ycgwl.com">dingxf</a> at 2018/10/19 0019
 */
public class AuthorityUtil {


    public static boolean validateIdentityKey(String idKeyString, Integer idKey){
        if(idKey == null || idKey <= 0 || StringUtils.isBlank(idKeyString) || idKey == CoreConstants.USER_CATEGORY_DRIVER){
            return true;
        }
        return Optional.ofNullable(StringUtils.integerCollection(idKeyString)).orElse(Collections.emptyList()).stream().anyMatch(c -> c - idKey == 0);
    }

    public static Collection<AuthorityMenu> filterAuthority(Collection<AuthorityMenu> collection, Predicate<AuthorityMenu> predicate){
        if(CollectionUtils.isNotEmpty(collection)){
            return collection.stream().filter(c-> filterAuthority(c, predicate)).collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    public static boolean filterAuthority(AuthorityMenu am, Predicate<AuthorityMenu> predicate){
        boolean flag = predicate.test(am);
        if(flag){
            if(CollectionUtils.isNotEmpty(am.getChildren())){
                am.setChildren(filterAuthority(am.getChildren(), predicate));
            }
            flag = am.available();
        }
        return flag;
    }

    private static <R> Collection<R> reduceAuthority(Collection<AuthorityMenu> authorityMenus, Collection<R> collection, Function<AuthorityMenu, R> mapper){
        if(CollectionUtils.isNotEmpty(authorityMenus)){
            return authorityMenus.stream().reduce(collection, (c, m) -> {
                c.add(mapper.apply(m));
                if(CollectionUtils.isNotEmpty(m.getChildren())){
                    c = Stream.concat(c.stream(), reduceAuthority(m.getChildren(), c, mapper).stream()).distinct().collect(Collectors.toList());
                }
                return c;
            }, (l, r) -> Stream.concat(l.stream(), r.stream()).collect(Collectors.toList()));
        }
        return Collections.emptyList();
    }
    public static <R> Collection<R> reduceAuthority(Collection<AuthorityMenu> authorityMenus, Function<AuthorityMenu, R> mapper){
        return reduceAuthority(authorityMenus, new ArrayList<R>(), mapper);
    }
    public static ArrayList<String> authorityMenuCode(ArrayList<String> collection, AuthorityMenu m){
        if(StringUtils.isNotBlank(m.getCode())){
            collection.add(m.getCode());
            for (AuthorityMenu child : Optional.ofNullable(m.getChildren()).orElse(Collections.emptyList())) {
                authorityMenuCode(collection, child);
            }
        }
        return collection;
    }

    public static void inspectEmployeeAuthority(Collection<AuthorityMenu> existsCollection, Predicate<AuthorityMenu> predicate){
        for (Iterator<AuthorityMenu> iterator = existsCollection.iterator(); iterator.hasNext(); ) {
            AuthorityMenu authority = iterator.next();
            if(CollectionUtils.isNotEmpty(authority.getChildren())){
                inspectEmployeeAuthority(authority.getChildren(), predicate);
            }
            if(predicate.test(authority)){
                iterator.remove();
            }
        }
    }

    public static Collection<AuthorityMenu> cycleConverts(Collection<SysMenu> collection, Predicate<SysMenu> predicate){
        if(CollectionUtils.isNotEmpty(collection)){
            Map<Integer, List<SysMenu>> groups = collection.stream().filter(predicate).collect(Collectors.groupingBy(SysMenu::getpId));
            if(groups != null && groups.size() > 0){
                return collection.stream().filter(m-> m.getpId() == 0).map(m-> cycleConvert(m, groups)).collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }

    private static AuthorityMenu cycleConvert(SysMenu m, Map<Integer, List<SysMenu>> groups){
        AuthorityMenu authority = new AuthorityMenu(m);
        Collection<SysMenu> children = groups.get(m.getId());
        if(CollectionUtils.isNotEmpty(children)){
            authority.setLeaf(false);
            authority.setChildren(children.stream().map(c-> cycleConvert(c, groups)).collect(Collectors.toList()));
        }else{
            authority.setLeaf(m.getpId() > 0);
            authority.setChildren(Collections.emptyList());
        }
        return authority;
    }
}
