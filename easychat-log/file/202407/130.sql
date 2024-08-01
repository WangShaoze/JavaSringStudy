

CREATE TABLE `user_info_beauty` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` varchar(12) NOT NULL COMMENT '用户ID',
  `email` varchar(50) NOT NULL COMMENT '邮箱',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态（0：未使用， 1：已使用）',
  PRIMARY KEY (`id`),
  UNIQUE KEY `idx_key_user_id` (`user_id`) USING BTREE,
  UNIQUE KEY `idx_key_email_id` (`email`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='靓号表';


CREATE TABLE `user_info` (
  `user_id` varchar(12) NOT NULL COMMENT '用户id',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `nick_name` varchar(20) DEFAULT NULL COMMENT '昵称',
  `join_type` tinyint(1) DEFAULT NULL COMMENT '加入类型（0：直接加入 1：同意后加好友）',
  `sex` tinyint(1) DEFAULT NULL COMMENT '性别（0：女，1：男）',
  `password` varchar(32) DEFAULT NULL COMMENT '密码',
  `personal_signature` varchar(100) DEFAULT NULL COMMENT '个性签名',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态(0: 禁用,1:启用)',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `last_login_time` datetime DEFAULT NULL COMMENT '最后登录时间',
  `area_name` varchar(50) DEFAULT NULL COMMENT '地区',
  `area_code` varchar(50) DEFAULT NULL COMMENT '地区编号',
  `last_off_time` bigint(13) DEFAULT NULL COMMENT '最后离开时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `idx_key_email` (`email`) USING BTREE COMMENT '邮箱必须唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户信息表';





CREATE TABLE `group_info` (
  `group_id` varchar(12) NOT NULL COMMENT '群ID',
  `group_name` varchar(20) DEFAULT NULL COMMENT '群组名',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `group_notice` varchar(500) DEFAULT NULL COMMENT '群公告',
  `join_type` tinyint(1) DEFAULT NULL COMMENT '0：直接加入1：管理员同意后加入',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态1:正常 0:解散',
  `group_owner_id` varchar(12) DEFAULT NULL COMMENT '群组id',
  PRIMARY KEY (`group_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;



CREATE TABLE `chat_session_user` (
  `user_id` varchar(12) NOT NULL COMMENT '用户ID',
  `contact_id` varchar(12) NOT NULL COMMENT '联系人ID',
  `session_id` varchar(32) NOT NULL COMMENT '会话ID',
  `contact_name` varchar(20) DEFAULT NULL COMMENT '联系人名称',
  PRIMARY KEY (`user_id`,`contact_id`) USING BTREE,
  KEY `idx_user_id` (`user_id`) USING BTREE,
  KEY `idx_session_id` (`session_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='会话用户';


CREATE TABLE `chat_session` (
  `session_id` varchar(32) NOT NULL COMMENT '会话ID',
  `last_message` varchar(500) DEFAULT NULL COMMENT '最后接收的消息',
  `last_receive_time` bigint(11) DEFAULT NULL COMMENT '最后接收消息时间毫秒',
  PRIMARY KEY (`session_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='会话信息';



CREATE TABLE `chat_message` (
  `message_id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '消息自增ID',
  `session_id` varchar(32) NOT NULL COMMENT '会话ID',
  `message_type` tinyint(1) NOT NULL COMMENT '消息类型',
  `message_content` varchar(500) DEFAULT NULL COMMENT '消息内容',
  `send_user_id` varchar(12) DEFAULT NULL COMMENT '发送人ID',
  `send_user_nick_name` varchar(20) DEFAULT NULL COMMENT '发送人昵称',
  `send_time` bigint(20) DEFAULT NULL COMMENT '发送时间',
  `contact_id` varchar(12) NOT NULL COMMENT '接收联系人ID',
  `contact_type` tinyint(1) DEFAULT NULL COMMENT '联系人类型: 0:单聊, 1:群聊',
  `file_size` bigint(28) DEFAULT NULL COMMENT '文件大小',
  `file_name` varchar(28) DEFAULT NULL COMMENT '文件名',
  `file_type` tinyint(1) DEFAULT NULL COMMENT '文件类型',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态: 0:正在发送, 1:已发送',
  PRIMARY KEY (`message_id`) USING BTREE,
  KEY `idx_session_id` (`session_id`) USING BTREE,
  KEY `idx_send_user_id` (`send_user_id`) USING BTREE,
  KEY `idx_receive_contact_id` (`contact_id`) USING BTREE,
  KEY `idx_send_time` (`send_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=70 DEFAULT CHARSET=utf8mb4 COMMENT='聊天消息表';


CREATE TABLE `user_contact` (
  `user_id` varchar(12) NOT NULL COMMENT '用户ID',
  `contact_id` varchar(12) NOT NULL COMMENT '联系人ID或者群组ID',
  `contact_type` tinyint(1) DEFAULT NULL COMMENT '联系人类型0:好友 1:群组',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态:(0:非好友1:好友2:已删除好友3：被好友删除4:已拉黑好友5:被好友拉黑)',
  `last_update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '最后更新时间',
  PRIMARY KEY (`user_id`,`contact_id`) USING BTREE,
  KEY `idx_contact_id` (`contact_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='联系人';





CREATE TABLE `user_contact_apply` (
  `apply_id` int(12) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `apply_user_id` varchar(12) NOT NULL COMMENT '申请人id',
  `receive_user_id` varchar(12) NOT NULL COMMENT '接收人ID',
  `contact_type` tinyint(1) NOT NULL COMMENT '联系人类型0:好友 1:群组',
  `contact_id` varchar(12) DEFAULT NULL COMMENT '联系人群组ID',
  `last_apply_time` bigint(20) DEFAULT NULL COMMENT '最后申请时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '状态0:待处理1:已同意2:已拒绝3:已拉黑',
  `apply_info` varchar(100) DEFAULT NULL COMMENT '申请信息',
  PRIMARY KEY (`apply_id`) USING BTREE,
  UNIQUE KEY `idx_key` (`apply_user_id`,`receive_user_id`,`contact_id`) USING BTREE,
  KEY `idx_last_apply_time` (`last_apply_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC COMMENT='联系人申请';





CREATE TABLE `app_update` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `version` varchar(10) DEFAULT NULL COMMENT '版本号',
  `update_desc` varchar(500) DEFAULT NULL COMMENT '更新描述',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `status` tinyint(1) DEFAULT NULL COMMENT '0:未发布1:灰度发布2:全网发布',
  `grayscale_uid` varchar(1000) DEFAULT NULL COMMENT '灰度uid',
  `file_type` tinyint(1) DEFAULT NULL COMMENT '文件类型0:本地文件 1:外链',
  `outer_link` varchar(200) DEFAULT NULL COMMENT '外链地址',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `idx_key` (`version`) COMMENT '版本唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='app发布';

