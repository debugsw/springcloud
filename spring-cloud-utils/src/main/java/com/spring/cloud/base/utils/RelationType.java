package com.spring.cloud.base.utils;

import com.spring.cloud.base.utils.abstra.Alias;
import com.spring.cloud.base.utils.abstra.Link;

/**
 * @Author: ls
 * @Description: 注解属性的关系类型
 * @Date: 2023/4/13 16:11
 */
public enum RelationType {

	/**
	 * <p>表示注解的属性与指定的属性互为镜像，通过一个属性将能够获得对方的值。<br>
	 * 它们遵循下述规则：
	 * <ul>
	 *     <li>互为镜像的两个属性，必须同时通过指定模式为{@code MIRROR_FOR}的{@link Link}注解指定对方；</li>
	 *     <li>互为镜像的两个属性，类型必须一致；</li>
	 *     <li>互为镜像的两个属性在获取值，且两者的值皆不同时，必须且仅允许有一个非默认值，该值被优先返回；</li>
	 *     <li>互为镜像的两个属性，在值都为默认值或都不为默认值时，两者的值必须相等；</li>
	 * </ul>
	 */
	MIRROR_FOR,

	/**
	 * <p>表示“原始属性”将作为“关联属性”的别名。
	 * <ul>
	 *     <li>当“原始属性”为默认值时，获取“关联属性”将返回“关联属性”本身的值；</li>
	 *     <li>当“原始属性”不为默认值时，获取“关联属性”将返回“原始属性”的值；</li>
	 * </ul>
	 */
	ALIAS_FOR,

	/**
	 * <p>表示“原始属性”将强制作为“关联属性”的别名。效果等同于在“原始属性”上添加{@link Alias}注解，
	 * 任何情况下，获取“关联属性”的值都将直接返回“原始属性”的值
	 */
	FORCE_ALIAS_FOR;

}
