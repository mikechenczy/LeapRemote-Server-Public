/*
 Navicat Premium Data Transfer

 Source Server         : Android
 Source Server Type    : MySQL
 Source Server Version : 80037
 Source Host           : 192.168.1.100:3306
 Source Schema         : jeecg-boot

 Target Server Type    : MySQL
 Target Server Version : 80037
 File Encoding         : 65001

 Date: 30/08/2024 07:40:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for leapremote_info
-- ----------------------------
DROP TABLE IF EXISTS `leapremote_info`;
CREATE TABLE `leapremote_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `description` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '描述',
  `content` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '内容',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 77 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of leapremote_info
-- ----------------------------
INSERT INTO `leapremote_info` VALUES (1, NULL, NULL, NULL, NULL, '应用名称', '飞跃云控');
INSERT INTO `leapremote_info` VALUES (2, NULL, NULL, NULL, NULL, '官方邮箱服务器地址', 'smtp.qq.com');
INSERT INTO `leapremote_info` VALUES (3, NULL, NULL, NULL, NULL, '官方邮箱', 'your_mail_here');
INSERT INTO `leapremote_info` VALUES (4, NULL, NULL, NULL, NULL, '最新版本', '4.7');
INSERT INTO `leapremote_info` VALUES (5, NULL, NULL, NULL, NULL, '最新版本描述', '新增邀请码功能，提供更多优惠');
INSERT INTO `leapremote_info` VALUES (6, NULL, NULL, NULL, NULL, '最新版本安卓', '1.1');
INSERT INTO `leapremote_info` VALUES (7, NULL, NULL, NULL, NULL, '最新版本描述安卓', '新增复制ip功能，支持仅直连开启下通过服务器获取ipv6地址进行连接');
INSERT INTO `leapremote_info` VALUES (8, NULL, NULL, NULL, NULL, '是否SSL加密', 'true');
INSERT INTO `leapremote_info` VALUES (9, NULL, NULL, NULL, NULL, '官方邮箱密码', 'your_password_here');
INSERT INTO `leapremote_info` VALUES (10, NULL, NULL, NULL, NULL, '单块下载大小', '40960');
INSERT INTO `leapremote_info` VALUES (11, NULL, NULL, NULL, NULL, 'Cookie时长', '2147483647');
INSERT INTO `leapremote_info` VALUES (12, NULL, NULL, NULL, NULL, '试用Vip时长', '7200000');
INSERT INTO `leapremote_info` VALUES (14, NULL, NULL, NULL, NULL, '电脑版下载地址', 'https://mjczy.top/leapremote');
INSERT INTO `leapremote_info` VALUES (15, NULL, NULL, NULL, NULL, '安卓版下载地址', 'https://mjczy.top/leapremote');
INSERT INTO `leapremote_info` VALUES (16, NULL, NULL, NULL, NULL, '公告', 'No message');
INSERT INTO `leapremote_info` VALUES (17, NULL, NULL, NULL, NULL, '域名', 'server.mjczy.top');
INSERT INTO `leapremote_info` VALUES (18, NULL, NULL, NULL, NULL, '支付过期时间', '1800000');
INSERT INTO `leapremote_info` VALUES (36, NULL, NULL, NULL, NULL, '签到时长', '300000');
INSERT INTO `leapremote_info` VALUES (37, NULL, NULL, NULL, NULL, '人类签到时长', '5min');
INSERT INTO `leapremote_info` VALUES (38, NULL, NULL, NULL, NULL, '微信支付是否可用', 'false');
INSERT INTO `leapremote_info` VALUES (40, NULL, NULL, NULL, NULL, '强制更新', 'false');
INSERT INTO `leapremote_info` VALUES (42, NULL, NULL, NULL, NULL, '强制更新安卓', 'false');
INSERT INTO `leapremote_info` VALUES (43, NULL, NULL, NULL, NULL, '支持的邮箱', '@126.com;@163.com;@qq.com;@sina.com;@sina.cn;@gmail.com;@outlook.com');
INSERT INTO `leapremote_info` VALUES (45, NULL, NULL, NULL, NULL, '公告安卓', 'No message');
INSERT INTO `leapremote_info` VALUES (47, NULL, NULL, NULL, NULL, '每天看广告最多次数', '7');
INSERT INTO `leapremote_info` VALUES (48, NULL, NULL, NULL, NULL, '时长与最多次数', '15min(每日最多6次)');
INSERT INTO `leapremote_info` VALUES (49, NULL, NULL, NULL, NULL, '广告VIP时长', '900000');
INSERT INTO `leapremote_info` VALUES (51, NULL, NULL, NULL, NULL, '刷新节点时长', '7200000');
INSERT INTO `leapremote_info` VALUES (52, NULL, NULL, NULL, NULL, '是否处于审核期', 'false');
INSERT INTO `leapremote_info` VALUES (53, NULL, NULL, NULL, NULL, '新活动', 'No new acts');
INSERT INTO `leapremote_info` VALUES (54, NULL, NULL, NULL, NULL, '广告是否可用', 'false');
INSERT INTO `leapremote_info` VALUES (55, NULL, NULL, NULL, NULL, '时长与最多次数英文', '15min(6 times every day)');
INSERT INTO `leapremote_info` VALUES (59, NULL, NULL, NULL, NULL, '账号删除时长', '15638400000');
INSERT INTO `leapremote_info` VALUES (60, NULL, NULL, NULL, NULL, '账号删除缓冲时长', '604800000');
INSERT INTO `leapremote_info` VALUES (61, NULL, NULL, NULL, NULL, '账号删除缓冲时长文本', '一周');
INSERT INTO `leapremote_info` VALUES (62, NULL, NULL, NULL, NULL, '预发布版本安卓', '1.2');
INSERT INTO `leapremote_info` VALUES (63, NULL, NULL, NULL, NULL, '预发布版本', '4.8');
INSERT INTO `leapremote_info` VALUES (64, NULL, NULL, NULL, NULL, 'STARTTLS', 'false');
INSERT INTO `leapremote_info` VALUES (65, NULL, NULL, NULL, NULL, '审核人类签到时长', '1h');
INSERT INTO `leapremote_info` VALUES (66, NULL, NULL, NULL, NULL, '审核签到时长', '3600000');
INSERT INTO `leapremote_info` VALUES (67, NULL, NULL, NULL, NULL, '审核员名单', '[\"test\"]');
INSERT INTO `leapremote_info` VALUES (70, NULL, NULL, NULL, NULL, '最新版本Java', 'Java-1.4');
INSERT INTO `leapremote_info` VALUES (71, NULL, NULL, NULL, NULL, '最新版本描述Java', '新增阿里dns服务器');
INSERT INTO `leapremote_info` VALUES (72, NULL, NULL, NULL, NULL, '强制更新Java', 'false');
INSERT INTO `leapremote_info` VALUES (73, NULL, NULL, NULL, NULL, '预发布版本Java', 'Java-1.4');
INSERT INTO `leapremote_info` VALUES (75, NULL, NULL, NULL, NULL, '邀请码时长', '259200000');
INSERT INTO `leapremote_info` VALUES (76, NULL, NULL, NULL, NULL, '邀请他人时长', '129600000');

-- ----------------------------
-- Table structure for leapremote_login_info
-- ----------------------------
DROP TABLE IF EXISTS `leapremote_login_info`;
CREATE TABLE `leapremote_login_info`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `username` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `last_time` datetime(6) NULL DEFAULT NULL COMMENT '最后登录时间',
  `location` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地区',
  `device` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备',
  `version` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '版本',
  `look_ads` int(0) NULL DEFAULT NULL COMMENT '看广告次数',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 828 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for leapremote_sign_in
-- ----------------------------
DROP TABLE IF EXISTS `leapremote_sign_in`;
CREATE TABLE `leapremote_sign_in`  (
  `id` bigint(0) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `create_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '创建人',
  `create_time` datetime(6) NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '更新人',
  `update_time` datetime(6) NULL DEFAULT NULL COMMENT '更新时间',
  `user_id` int(0) NULL DEFAULT NULL COMMENT '用户ID',
  `username` varchar(254) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 156 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for leapremote_user_data
-- ----------------------------
DROP TABLE IF EXISTS `leapremote_user_data`;
CREATE TABLE `leapremote_user_data`  (
  `device_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL,
  `user_id` int(0) NULL DEFAULT NULL,
  `connect_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `connect_pin` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `control_id` int(0) NULL DEFAULT NULL,
  `devices` blob NULL,
  `last_login` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `version` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ip` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `device_info` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `ips` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`device_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
