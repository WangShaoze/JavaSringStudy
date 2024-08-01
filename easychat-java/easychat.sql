/*
 Navicat Premium Data Transfer

 Source Server         : LocalDB
 Source Server Type    : MySQL
 Source Server Version : 50713
 Source Host           : localhost:3306
 Source Schema         : easychat

 Target Server Type    : MySQL
 Target Server Version : 50713
 File Encoding         : 65001

 Date: 01/08/2024 08:13:32
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for app_update
-- ----------------------------
DROP TABLE IF EXISTS `app_update`;
CREATE TABLE `app_update`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `version` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '版本号',
  `update_desc` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '更新描述',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '0:未发布1:灰度发布2:全网发布',
  `grayscale_uid` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '灰度uid',
  `file_type` tinyint(1) NULL DEFAULT NULL COMMENT '文件类型0:本地文件 1:外链',
  `outer_link` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '外链地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_key`(`version`) USING BTREE COMMENT '版本唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'app发布' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of app_update
-- ----------------------------
INSERT INTO `app_update` VALUES (5, '1.0.1', '首次发布|开源免费|多平台服务', '2024-07-02 08:42:56', 1, 'U38760487295,U27996346096,U77772181769', 1, 'http:www.baidu.com');

-- ----------------------------
-- Table structure for chat_message
-- ----------------------------
DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`  (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息自增ID',
  `session_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `message_type` tinyint(1) NOT NULL COMMENT '消息类型',
  `message_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '消息内容',
  `send_user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人ID',
  `send_user_nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '发送人昵称',
  `send_time` bigint(20) NULL DEFAULT NULL COMMENT '发送时间',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收联系人ID',
  `contact_type` tinyint(1) NULL DEFAULT NULL COMMENT '联系人类型: 0:单聊, 1:群聊',
  `file_size` bigint(28) NULL DEFAULT NULL COMMENT '文件大小',
  `file_name` varchar(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '文件名',
  `file_type` tinyint(1) NULL DEFAULT NULL COMMENT '文件类型',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态: 0:正在发送, 1:已发送',
  PRIMARY KEY (`message_id`) USING BTREE,
  INDEX `idx_session_id`(`session_id`) USING BTREE,
  INDEX `idx_send_user_id`(`send_user_id`) USING BTREE,
  INDEX `idx_receive_contact_id`(`contact_id`) USING BTREE,
  INDEX `idx_send_time`(`send_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 169 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '聊天消息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_message
-- ----------------------------
INSERT INTO `chat_message` VALUES (70, '70c15b11b580c9dad11bf6d8a681b0db', 2, '欢迎使用EasyChat', 'Urobot', NULL, 1719819620840, 'Urobot', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (71, '96f8ada578f24e0f71d6327da6a09f2f', 3, '群组已经创建好，可以和好友一起畅聊了', NULL, NULL, 1719824113633, 'G74700138542', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (72, '96f8ada578f24e0f71d6327da6a09f2f', 2, '这个群专用于英语学习及交流', 'U38760487295', '小王', 1719824187162, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (73, '70c15b11b580c9dad11bf6d8a681b0db', 2, '你好', 'U38760487295', '小王', 1719824200355, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (74, '70c15b11b580c9dad11bf6d8a681b0db', 2, 'I\'m EasyChat Robot, but I can\'t reply with you anything now.', 'Urobot', 'EasyChat', 1719824200371, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (75, 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 2, '欢迎使用EasyChat', 'Urobot', NULL, 1719824310271, 'Urobot', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (76, '96f8ada578f24e0f71d6327da6a09f2f', 9, '测试员test02加入了群组', NULL, NULL, 1719824473278, 'G74700138542', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (77, 'add5d66fa4468407513d2cbfd48ca001', 1, '我是测试员test02', 'U27996346096', '测试员test02', 1719824477278, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (78, 'add5d66fa4468407513d2cbfd48ca001', 2, '你好', 'U38760487295', '小王', 1719825100604, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (79, '96f8ada578f24e0f71d6327da6a09f2f', 2, '测试员test02 欢迎你加入我们', 'U38760487295', '小王', 1719825131185, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (80, '96f8ada578f24e0f71d6327da6a09f2f', 2, '我是群组', 'U38760487295', '小王', 1719825140996, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (81, '70c15b11b580c9dad11bf6d8a681b0db', 2, '哈哈', 'U38760487295', '小王', 1719825151597, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (82, '70c15b11b580c9dad11bf6d8a681b0db', 2, 'I\'m EasyChat Robot, but I can\'t reply with you anything now.', 'Urobot', 'EasyChat', 1719825151619, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (83, '96f8ada578f24e0f71d6327da6a09f2f', 2, '哈哈哈', 'U38760487295', '小王', 1719827976202, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (84, '96f8ada578f24e0f71d6327da6a09f2f', 2, '大家好呀', 'U38760487295', '小王', 1719827981316, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (85, 'add5d66fa4468407513d2cbfd48ca001', 2, '我是小王超管', 'U38760487295', '小王', 1719828039420, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (86, 'e5a8f5430281781e52872d0979d428d2', 3, '群组已经创建好，可以和好友一起畅聊了', NULL, NULL, 1719828196917, 'G49717928480', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (87, '70c15b11b580c9dad11bf6d8a681b0db', 2, '扣扣', 'U38760487295', '小王', 1719828221568, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (88, '70c15b11b580c9dad11bf6d8a681b0db', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', 'EasyChat', 1719828221594, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (89, '96f8ada578f24e0f71d6327da6a09f2f', 2, '你是群组', 'U27996346096', '测试员test02', 1719829172743, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (90, '96f8ada578f24e0f71d6327da6a09f2f', 2, '？？', 'U27996346096', '测试员test02', 1719829175854, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (91, '96f8ada578f24e0f71d6327da6a09f2f', 2, '😅', 'U27996346096', '测试员test02', 1719829185936, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (92, '96f8ada578f24e0f71d6327da6a09f2f', 2, '你是群主呀', 'U27996346096', '测试员test02', 1719829195404, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (93, 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 2, 'hello,可以和你互动吗？', 'U27996346096', '测试员test02', 1719829221636, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (94, 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', 'EasyChat', 1719829221675, 'U27996346096', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (95, 'add5d66fa4468407513d2cbfd48ca001', 2, '你需要注意你在群里的言行哦', 'U38760487295', '小王', 1719829834453, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (96, 'add5d66fa4468407513d2cbfd48ca001', 2, '欧克', 'U38760487295', '小王', 1719829938907, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (97, 'e5a8f5430281781e52872d0979d428d2', 9, '2号测试员加入了群组', NULL, NULL, 1719830217190, 'G49717928480', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (98, 'e5a8f5430281781e52872d0979d428d2', 2, '大家好呀', 'U27996346096', '2号测试员', 1719830348979, 'G49717928480', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (99, '96f8ada578f24e0f71d6327da6a09f2f', 2, '你能看见我给你发的消息吗', 'U27996346096', '2号测试员', 1719830377089, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (100, 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 2, '还留了', 'U27996346096', '2号测试员', 1719830385299, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (101, 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', 'EasyChat', 1719830385320, 'U27996346096', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (102, 'add5d66fa4468407513d2cbfd48ca001', 2, 'hjkhgkhkjh', 'U38760487295', '小王', 1719830540316, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (103, 'e5a8f5430281781e52872d0979d428d2', 2, '😁😁', 'U38760487295', '小王', 1719830565942, 'G49717928480', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (104, 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 2, 'dsada', 'U27996346096', '2号测试员', 1719830641054, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (105, 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', 'EasyChat', 1719830641069, 'U27996346096', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (106, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'fdsfs', 'U27996346096', '2号测试员', 1719830798354, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (107, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'hhhh', 'U38760487295', '小王', 1719830806407, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (108, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'hi hi hi', 'U27996346096', '2号测试员', 1719830860947, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (109, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'fdsfds', 'U38760487295', '小王', 1719830864806, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (110, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'fdsafdsa', 'U38760487295', '小王', 1719830868953, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (111, '96f8ada578f24e0f71d6327da6a09f2f', 2, '恢复供电和规范☹️', 'U27996346096', '2号测试员', 1719830876373, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (112, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'fdsafdsa🤒', 'U38760487295', '小王', 1719830886462, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (113, '96f8ada578f24e0f71d6327da6a09f2f', 5, '[图片]', 'U38760487295', '小王', 1719830890929, 'G74700138542', 1, 173393, 'icon.png', 0, 1);
INSERT INTO `chat_message` VALUES (114, '96f8ada578f24e0f71d6327da6a09f2f', 2, '撒旦', 'U27996346096', '2号测试员', 1719830898379, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (115, '77bb3d674156fc11969b322b25b130ca', 2, '欢迎使用EasyChat', 'Urobot', NULL, 1719830984352, 'Urobot', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (116, '96f8ada578f24e0f71d6327da6a09f2f', 9, 'test03加入了群组', NULL, NULL, 1719831048665, 'G74700138542', 1, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (117, '96f8ada578f24e0f71d6327da6a09f2f', 2, '大家好', 'U77772181769', 'test03', 1719831060688, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (118, '96f8ada578f24e0f71d6327da6a09f2f', 2, '范德萨范德萨', 'U77772181769', '3号测试员', 1719831130669, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (119, '96f8ada578f24e0f71d6327da6a09f2f', 2, '你是大胸姐', 'U38760487295', '小王', 1719831142069, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (120, '96f8ada578f24e0f71d6327da6a09f2f', 2, '哈哈哈', 'U77772181769', '3号测试员', 1719831147428, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (121, '96f8ada578f24e0f71d6327da6a09f2f', 2, '你是会说话的', 'U77772181769', '3号测试员', 1719831157649, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (122, '96f8ada578f24e0f71d6327da6a09f2f', 2, '你小子', 'U38760487295', '小王', 1719831166729, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (123, '96f8ada578f24e0f71d6327da6a09f2f', 2, '你姐妹', 'U38760487295', '小王', 1719831173806, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (124, '96f8ada578f24e0f71d6327da6a09f2f', 2, '好吧', 'U38760487295', '小王', 1719831187669, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (125, '96f8ada578f24e0f71d6327da6a09f2f', 2, '&lt;script>alert(\"你真好呀！\")&lt;/script>', 'U38760487295', '小王', 1719831227466, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (126, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 1, '我是3号测试员', 'U77772181769', '3号测试员', 1719831260816, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (127, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, '你好呀，大胸姐', 'U38760487295', '小王', 1719831275490, 'U77772181769', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (128, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, '你这个坏小子', 'U77772181769', '3号测试员', 1719831288984, 'U38760487295', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (129, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, '是咯，不过你说的确实也没啥错😁', 'U77772181769', '3号测试员', 1719831316118, 'U38760487295', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (130, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 5, '[文件]', 'U38760487295', '小王', 1719831347530, 'U77772181769', 0, 6785, '建表.sql', 2, 1);
INSERT INTO `chat_message` VALUES (131, '77bb3d674156fc11969b322b25b130ca', 2, '扣扣', 'U77772181769', '3号测试员', 1719831411373, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (132, '77bb3d674156fc11969b322b25b130ca', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', 'EasyChat', 1719831411394, 'U77772181769', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (133, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 5, '[图片]', 'U77772181769', '3号测试员', 1719831482950, 'U38760487295', 0, 57785, '8-22100Q33351.jpg', 0, 1);
INSERT INTO `chat_message` VALUES (134, '77bb3d674156fc11969b322b25b130ca', 2, '你好呀', 'U77772181769', '3号测试员', 1719832241458, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (135, '77bb3d674156fc11969b322b25b130ca', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719832241476, 'U77772181769', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (136, '77bb3d674156fc11969b322b25b130ca', 2, '欧克', 'U77772181769', '3号测试员', 1719832299339, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (137, '77bb3d674156fc11969b322b25b130ca', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719832299359, 'U77772181769', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (138, '77bb3d674156fc11969b322b25b130ca', 2, '是的', 'U77772181769', '3号测试员', 1719832317136, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (139, '77bb3d674156fc11969b322b25b130ca', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719832317158, 'U77772181769', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (140, '77bb3d674156fc11969b322b25b130ca', 2, 'ok', 'U77772181769', '3号测试员', 1719832386538, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (141, '77bb3d674156fc11969b322b25b130ca', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719832386567, 'U77772181769', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (142, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'koko', 'U77772181769', '3号测试员', 1719832556950, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (143, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'fdsafda', 'U38760487295', '小王', 1719832584807, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (144, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, 'ok', 'U38760487295', '小王', 1719832589830, 'U77772181769', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (145, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'koko', 'U77772181769', '3号测试员', 1719832669168, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (146, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, 'kokoo', 'U77772181769', '3号测试员', 1719832678750, 'U38760487295', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (147, '77bb3d674156fc11969b322b25b130ca', 2, 'fdsafds', 'U77772181769', '3号测试员', 1719832823048, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (148, '77bb3d674156fc11969b322b25b130ca', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719832823063, 'U77772181769', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (149, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, 'fdsafdsa', 'U77772181769', '3号测试员', 1719832827959, 'U38760487295', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (150, '70c15b11b580c9dad11bf6d8a681b0db', 2, '哈哈哈', 'U38760487295', '小王', 1719878064046, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (151, '70c15b11b580c9dad11bf6d8a681b0db', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719878064073, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (152, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, 'yes', 'U38760487295', '小王', 1719878073269, 'U77772181769', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (153, 'add5d66fa4468407513d2cbfd48ca001', 2, 'dsd', 'U38760487295', '小王', 1719878082034, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (154, '96f8ada578f24e0f71d6327da6a09f2f', 5, '[图片]', 'U38760487295', '小王', 1719881909120, 'G74700138542', 1, 52178, 'yo3yzttdkss.jpg', 0, 1);
INSERT INTO `chat_message` VALUES (155, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'fdsafdasfas', 'U27996346096', '2号测试员', 1719884842902, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (156, 'add5d66fa4468407513d2cbfd48ca001', 2, 'fdsafasghfagfjdkas', 'U38760487295', '小王', 1719884978556, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (157, 'add5d66fa4468407513d2cbfd48ca001', 2, 'fdsafdasfdsa', 'U38760487295', '小王', 1719884982646, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (158, 'add5d66fa4468407513d2cbfd48ca001', 2, 'fdsafsadfsa', 'U38760487295', '小王', 1719884989197, 'U27996346096', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (159, 'add5d66fa4468407513d2cbfd48ca001', 5, '[图片]', 'U38760487295', '小王', 1719884994947, 'U27996346096', 0, 47720, 'cym0wog2twq.jpg', 0, 1);
INSERT INTO `chat_message` VALUES (160, '96f8ada578f24e0f71d6327da6a09f2f', 2, 'fdsafasdf', 'U27996346096', '2号测试员', 1719885180192, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (161, '70c15b11b580c9dad11bf6d8a681b0db', 2, 'fdsafsdafdsfdsa', 'U38760487295', '小王', 1719885212720, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (162, '70c15b11b580c9dad11bf6d8a681b0db', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719885212737, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (163, '70c15b11b580c9dad11bf6d8a681b0db', 2, 'kokoooo', 'U38760487295', '小王', 1719927847895, 'Urobot', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (164, '70c15b11b580c9dad11bf6d8a681b0db', 2, '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 'Urobot', '小亦', 1719927847919, 'U38760487295', 0, NULL, NULL, NULL, 1);
INSERT INTO `chat_message` VALUES (165, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, '你是', 'U38760487295', '小王', 1719927858102, 'U77772181769', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (166, '96f8ada578f24e0f71d6327da6a09f2f', 2, '哈哈哈，这是什么', 'U77772181769', '3号测试员', 1719928063549, 'G74700138542', 1, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (167, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, '生活就是那么苟且', 'U77772181769', '3号测试员', 1719928240712, 'U38760487295', 0, NULL, '', NULL, 1);
INSERT INTO `chat_message` VALUES (168, 'e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', 2, '不是的哦', 'U38760487295', '小王', 1719928261919, 'U77772181769', 0, NULL, '', NULL, 1);

-- ----------------------------
-- Table structure for chat_session
-- ----------------------------
DROP TABLE IF EXISTS `chat_session`;
CREATE TABLE `chat_session`  (
  `session_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `last_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '最后接收的消息',
  `last_receive_time` bigint(11) NULL DEFAULT NULL COMMENT '最后接收消息时间毫秒',
  PRIMARY KEY (`session_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会话信息' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of chat_session
-- ----------------------------
INSERT INTO `chat_session` VALUES ('70c15b11b580c9dad11bf6d8a681b0db', '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 1719927847919);
INSERT INTO `chat_session` VALUES ('77bb3d674156fc11969b322b25b130ca', '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 1719832823063);
INSERT INTO `chat_session` VALUES ('96f8ada578f24e0f71d6327da6a09f2f', '3号测试员:哈哈哈，这是什么', 1719928063549);
INSERT INTO `chat_session` VALUES ('add5d66fa4468407513d2cbfd48ca001', '[图片]', 1719884994947);
INSERT INTO `chat_session` VALUES ('e3dc9bd7aa36ebdf0dbb20b6ab1cf4f5', '不是的哦', 1719928261919);
INSERT INTO `chat_session` VALUES ('e5a8f5430281781e52872d0979d428d2', '小王:😁😁', 1719830565942);
INSERT INTO `chat_session` VALUES ('f65e7dcd4e41e6ca80bb3504a6fe5fb2', '我是EasyChat聊天机器人,接入AI接口以后我可以为你服务。', 1719830641069);

-- ----------------------------
-- Table structure for chat_session_user
-- ----------------------------
DROP TABLE IF EXISTS `chat_session_user`;
CREATE TABLE `chat_session_user`  (
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人ID',
  `session_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '会话ID',
  `contact_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人名称',
  PRIMARY KEY (`user_id`, `contact_id`) USING BTREE,
  INDEX `idx_user_id`(`user_id`) USING BTREE,
  INDEX `idx_session_id`(`session_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会话用户' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of chat_session_user
-- ----------------------------
INSERT INTO `chat_session_user` VALUES ('U27996346096', 'G49717928480', 'e5a8f5430281781e52872d0979d428d2', '英语学习者交流2群');
INSERT INTO `chat_session_user` VALUES ('U27996346096', 'G74700138542', '96f8ada578f24e0f71d6327da6a09f2f', '英语学习者交流1群');
INSERT INTO `chat_session_user` VALUES ('U27996346096', 'Urobot', 'f65e7dcd4e41e6ca80bb3504a6fe5fb2', 'EasyChat');
INSERT INTO `chat_session_user` VALUES ('U38760487295', 'G49717928480', 'e5a8f5430281781e52872d0979d428d2', '英语学习者交流2群');
INSERT INTO `chat_session_user` VALUES ('U38760487295', 'G74700138542', '96f8ada578f24e0f71d6327da6a09f2f', '英语学习者交流1群');
INSERT INTO `chat_session_user` VALUES ('U38760487295', 'Urobot', '70c15b11b580c9dad11bf6d8a681b0db', 'EasyChat');
INSERT INTO `chat_session_user` VALUES ('U77772181769', 'G74700138542', '96f8ada578f24e0f71d6327da6a09f2f', '英语学习者交流1群');
INSERT INTO `chat_session_user` VALUES ('U77772181769', 'Urobot', '77bb3d674156fc11969b322b25b130ca', 'EasyChat');

-- ----------------------------
-- Table structure for group_info
-- ----------------------------
DROP TABLE IF EXISTS `group_info`;
CREATE TABLE `group_info`  (
  `group_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '群ID',
  `group_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群组名',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `group_notice` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群公告',
  `join_type` tinyint(1) NULL DEFAULT NULL COMMENT '0：直接加入1：管理员同意后加入',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态1:正常 0:解散',
  `group_owner_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '群组id',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of group_info
-- ----------------------------
INSERT INTO `group_info` VALUES ('G49717928480', '英语学习者交流2群', '2024-07-01 18:03:17', '主打一个学习轻松', 0, 1, 'U38760487295');
INSERT INTO `group_info` VALUES ('G74700138542', '英语学习者交流1群', '2024-07-01 16:55:14', '专用于英语学习者交流', 0, 1, 'U38760487295');

-- ----------------------------
-- Table structure for user_contact
-- ----------------------------
DROP TABLE IF EXISTS `user_contact`;
CREATE TABLE `user_contact`  (
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '联系人ID或者群组ID',
  `contact_type` tinyint(1) NULL DEFAULT NULL COMMENT '联系人类型0:好友 1:群组',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态:(0:非好友1:好友2:已删除好友3：被好友删除4:已拉黑好友5:被好友拉黑)',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`, `contact_id`) USING BTREE,
  INDEX `idx_contact_id`(`contact_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '联系人' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_contact
-- ----------------------------
INSERT INTO `user_contact` VALUES ('U27996346096', 'G49717928480', 1, '2024-07-01 18:36:57', 1, '2024-07-01 18:36:57');
INSERT INTO `user_contact` VALUES ('U27996346096', 'G74700138542', 1, '2024-07-01 17:01:13', 1, '2024-07-01 17:01:13');
INSERT INTO `user_contact` VALUES ('U27996346096', 'U38760487295', 0, '2024-07-01 17:01:17', 1, '2024-07-01 17:01:17');
INSERT INTO `user_contact` VALUES ('U27996346096', 'Urobot', 0, '2024-07-01 16:58:30', 1, '2024-07-01 16:58:30');
INSERT INTO `user_contact` VALUES ('U38760487295', 'G49717928480', 1, '2024-07-01 18:03:17', 1, '2024-07-01 18:03:17');
INSERT INTO `user_contact` VALUES ('U38760487295', 'G74700138542', 1, '2024-07-01 16:55:14', 1, '2024-07-01 16:55:14');
INSERT INTO `user_contact` VALUES ('U38760487295', 'U27996346096', 0, '2024-07-01 17:01:17', 1, '2024-07-01 17:01:17');
INSERT INTO `user_contact` VALUES ('U38760487295', 'U77772181769', 0, '2024-07-01 18:54:21', 1, '2024-07-01 18:54:21');
INSERT INTO `user_contact` VALUES ('U38760487295', 'Urobot', 0, '2024-07-01 15:40:21', 1, '2024-07-01 15:40:21');
INSERT INTO `user_contact` VALUES ('U77772181769', 'G74700138542', 1, '2024-07-01 18:50:49', 1, '2024-07-01 18:50:49');
INSERT INTO `user_contact` VALUES ('U77772181769', 'U38760487295', 0, '2024-07-01 18:54:21', 1, '2024-07-01 18:54:21');
INSERT INTO `user_contact` VALUES ('U77772181769', 'Urobot', 0, '2024-07-01 18:49:44', 1, '2024-07-01 18:49:44');

-- ----------------------------
-- Table structure for user_contact_apply
-- ----------------------------
DROP TABLE IF EXISTS `user_contact_apply`;
CREATE TABLE `user_contact_apply`  (
  `apply_id` int(12) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `apply_user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '申请人id',
  `receive_user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '接收人ID',
  `contact_type` tinyint(1) NOT NULL COMMENT '联系人类型0:好友 1:群组',
  `contact_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '联系人群组ID',
  `last_apply_time` bigint(20) NULL DEFAULT NULL COMMENT '最后申请时间',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态0:待处理1:已同意2:已拒绝3:已拉黑',
  `apply_info` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '申请信息',
  PRIMARY KEY (`apply_id`) USING BTREE,
  UNIQUE INDEX `idx_key`(`apply_user_id`, `receive_user_id`, `contact_id`) USING BTREE,
  INDEX `idx_last_apply_time`(`last_apply_time`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '联系人申请' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_contact_apply
-- ----------------------------
INSERT INTO `user_contact_apply` VALUES (23, 'U27996346096', 'U38760487295', 0, 'U38760487295', 1719824477273, 1, '我是测试员test02');
INSERT INTO `user_contact_apply` VALUES (24, 'U27996346096', 'U38760487295', 1, 'G74700138542', 1719824473270, 1, '我是测试员test02,我想学习英语');
INSERT INTO `user_contact_apply` VALUES (25, 'U77772181769', 'U38760487295', 0, 'U38760487295', 1719831260812, 1, '我是3号测试员');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户id',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `join_type` tinyint(1) NULL DEFAULT NULL COMMENT '加入类型（0：直接加入 1：同意后加好友）',
  `sex` tinyint(1) NULL DEFAULT NULL COMMENT '性别（0：女，1：男）',
  `password` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '密码',
  `personal_signature` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '个性签名',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态(0: 禁用,1:启用)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `last_login_time` datetime NULL DEFAULT NULL COMMENT '最后登录时间',
  `area_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地区',
  `area_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '地区编号',
  `last_off_time` bigint(13) NULL DEFAULT NULL COMMENT '最后离开时间',
  PRIMARY KEY (`user_id`) USING BTREE,
  UNIQUE INDEX `idx_key_email`(`email`) USING BTREE COMMENT '邮箱必须唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('U27996346096', 'test02@qq.com', '2号测试员', 1, 1, '9aa4fc83da564e85e9fcc9aaa5c76f1d', '专稿测试的测试员test02', 1, '2024-07-01 16:58:30', '2024-07-02 09:51:16', '广东省,珠海市', '10004,10009', 1719885233224);
INSERT INTO `user_info` VALUES ('U38760487295', 'test01@qq.com', '小王', 1, 1, '84cc106bbca00a2a1d753e3c0ed0be41', '我就是我，谁都替代不了', 1, '2024-07-01 15:40:21', '2024-07-02 21:41:29', '云南省,曲靖市', '10221,10223', 1719934289097);
INSERT INTO `user_info` VALUES ('U77772181769', 'test03@qq.com', '3号测试员', 1, 0, '0515db26563dc09330871b995fb0bb98', '我是一个御姐', 1, '2024-07-01 18:49:44', '2024-07-02 21:42:01', '广东省,深圳市', '10004,10006', 1719934290975);

-- ----------------------------
-- Table structure for user_info_beauty
-- ----------------------------
DROP TABLE IF EXISTS `user_info_beauty`;
CREATE TABLE `user_info_beauty`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(12) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户ID',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '邮箱',
  `status` tinyint(1) NULL DEFAULT NULL COMMENT '状态（0：未使用， 1：已使用）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `idx_key_user_id`(`user_id`) USING BTREE,
  UNIQUE INDEX `idx_key_email_id`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '靓号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of user_info_beauty
-- ----------------------------
INSERT INTO `user_info_beauty` VALUES (2, '88888888888', 'test05@qq.com', 0);
INSERT INTO `user_info_beauty` VALUES (3, '68686868686', 'test07@qq.com', 0);
INSERT INTO `user_info_beauty` VALUES (4, '99999999999', 'test06@qq.com', 0);

SET FOREIGN_KEY_CHECKS = 1;
