/*
Navicat MySQL Data Transfer

Source Server         : chap01
Source Server Version : 50744
Source Host           : localhost:3306
Source Database       : tourism_platform

Target Server Type    : MYSQL
Target Server Version : 50744
File Encoding         : 65001

Date: 2026-04-27 22:07:38
*/

SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `announcement`
-- ----------------------------
DROP TABLE IF EXISTS `announcement`;
CREATE TABLE `announcement` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `title` varchar(200) NOT NULL COMMENT '标题',
  `category` varchar(50) DEFAULT NULL COMMENT '分类：festival-节庆活动，promotion-优惠活动，news-新闻资讯',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `content` longtext NOT NULL COMMENT '内容',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否置顶：0-否，1-是',
  `is_banner` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否轮播：0-否，1-是',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_is_banner` (`is_banner`),
  KEY `idx_start_time` (`start_time`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COMMENT='活动公告表';

-- ----------------------------
-- Records of announcement
-- ----------------------------
INSERT INTO `announcement` VALUES ('1', '112123', 'promotion', 'https://images.pexels.com/photos/35464172/pexels-photo-35464172.jpeg', '123123', '2026-01-05 12:47:52', '2026-01-30 00:00:00', '0', '1', '0', '1', '1', '2026-01-05 12:47:58', '2026-01-05 12:47:58');
INSERT INTO `announcement` VALUES ('3', '太原旅游一卡通开始办理', 'promotion', 'https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg', '2026年太原旅游一卡通现已开始办理，售价98元/张，可全年免费游览太原及周边20余个景区，办理地点：太原市各区县旅游局、各大景区售票处。', '2026-01-01 00:00:00', '2026-12-31 00:00:00', '0', '1', '0', '1', '90', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `announcement` VALUES ('4', '太原旅游一卡通开始办理', 'promotion', 'https://images.pexels.com/photos/35199314/pexels-photo-35199314.jpeg', '2026年太原旅游一卡通现已开始办理，售价98元/张，可全年免费游览太原及周边20余个景区，办理地点：太原市各区县旅游局、各大景区售票处。', '2026-01-01 00:00:00', '2026-12-31 00:00:00', '0', '1', '0', '1', '70', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `announcement` VALUES ('5', '食品街升级改造完成 正式开放', 'promotion', 'https://images.pexels.com/photos/35134367/pexels-photo-35134367.jpeg', '太原食品街升级改造工程已全部完成，于2026年1月10日正式开放，新增特色小吃店铺20余家，优化了游览环境，提升了游客体验。', '2026-01-10 00:00:00', '2026-02-10 00:00:00', '1', '1', '0', '1', '9', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `announcement` VALUES ('7', '太原植物园春季花展即将开展', 'festival', 'https://images.pexels.com/photos/14686356/pexels-photo-14686356.jpeg', '太原植物园2026年春季花展将于3月1日至4月30日举办，展出各类花卉200余种，包括郁金香、樱花、海棠等，门票价格不变，欢迎广大游客前来观赏。', '2026-02-20 00:00:00', '2026-04-30 00:00:00', '0', '1', '0', '1', '79', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `announcement` VALUES ('8', '2024年太原旅游节盛大开幕', 'festival', 'https://images.pexels.com/photos/16197273/pexels-photo-16197273.jpeg', '2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。2024年太原旅游节盛大开幕的详细内容。欢迎广大游客前来参观游览。', '2026-01-06 15:51:44', '2026-02-28 15:51:44', '1', '1', '1673', '1', '72', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `announcement` VALUES ('9', '晋祠景区门票优惠活动', 'promotion', '/uploads/images/announcement_2.jpg', '晋祠景区门票优惠活动的详细内容。欢迎广大游客前来参观游览。', '2026-01-28 15:51:44', '2026-04-03 15:51:44', '0', '1', '1937', '1', '44', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `announcement` VALUES ('11', '春季旅游优惠季开始', 'promotion', 'https://images.pexels.com/photos/8540878/pexels-photo-8540878.jpeg', '春季旅游优惠季开始的详细内容。欢迎广大游客前来参观游览。', '2026-01-05 15:51:44', '2026-03-21 15:51:44', '0', '1', '790', '1', '75', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `announcement` VALUES ('12', '太原古县城文化节即将举办', 'festival', '/uploads/images/announcement_5.jpg', '太原古县城文化节即将举办的详细内容。欢迎广大游客前来参观游览。', '2026-01-28 15:51:44', '2026-04-27 15:51:44', '0', '1', '333', '1', '54', '2026-01-28 15:51:44', '2026-01-28 15:51:44');

-- ----------------------------
-- Table structure for `attraction`
-- ----------------------------
DROP TABLE IF EXISTS `attraction`;
CREATE TABLE `attraction` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '景点ID',
  `name` varchar(100) NOT NULL COMMENT '景点名称',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建用户ID（可为空，系统创建）',
  `description` text COMMENT '景点描述',
  `content` longtext COMMENT '详细内容（富文本）',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `opening_hours` varchar(100) DEFAULT NULL COMMENT '开放时间',
  `ticket_price` decimal(10,2) DEFAULT NULL COMMENT '门票价格',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '评分（0-5）',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `favorite_count` int(11) NOT NULL DEFAULT '0' COMMENT '收藏数',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`),
  FULLTEXT KEY `ft_name_desc` (`name`,`description`),
  CONSTRAINT `fk_attraction_category` FOREIGN KEY (`category_id`) REFERENCES `attraction_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COMMENT='景点表';

-- ----------------------------
-- Records of attraction
-- ----------------------------
INSERT INTO `attraction` VALUES ('2', '双塔博物馆', null, null, '', null, 'https://images.pexels.com/photos/34922354/pexels-photo-34922354.jpeg', null, '山西太原迎泽区太原市双塔博物馆', '112.5799600', '37.8437540', '全天', '38.00', '0.00', '10', '0', '0', null, '1', '0', '2026-01-05 13:32:35', '2026-01-05 13:32:35');
INSERT INTO `attraction` VALUES ('3', '太原万象城', null, '4', '', '', 'https://images.pexels.com/photos/34652802/pexels-photo-34652802.jpeg', null, '山西省太原市和平区长风商务区长兴路5号', '112.5292210', '37.8077010', '', '0.00', '0.00', '18', '0', '1', null, '1', '0', '2026-01-05 14:36:39', '2026-01-05 14:36:39');
INSERT INTO `attraction` VALUES ('4', 'Church', null, null, '', '', 'https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg', null, '山西省太原市杏花岭区新建路69号', '112.5496560', '37.8704510', '', '10.00', '0.00', '8', '0', '1', null, '1', '0', '2026-01-05 15:55:30', '2026-01-05 15:55:30');
INSERT INTO `attraction` VALUES ('5', '摩天轮', null, '4', '山西省太原市中北大学', '', '', null, '中北大学', '112.4487600', '38.0131030', '', '0.00', '0.00', '5', '0', '0', null, '1', '0', '2026-01-09 16:45:22', '2026-01-09 16:45:22');
INSERT INTO `attraction` VALUES ('7', '晋祠博物馆老馆', '2', null, '中国现存最早的皇家祭祀园林，集中国古代祭祀建筑、园林、雕塑、壁画、碑刻艺术为一体', null, null, null, '山西省太原市晋源区晋祠镇', '112.4522300', '37.7964500', '08:00-18:00', '80.00', '0.00', '0', '0', '0', '历史古迹,人文景观,晋文化', '1', '5', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('8', '晋祠博物馆南区', '2', null, '中国现存最早的皇家祭祀园林，集中国古代祭祀建筑、园林、雕塑、壁画、碑刻艺术为一体', null, null, null, '山西省太原市晋源区晋祠镇', '112.4522300', '37.7964500', '08:00-18:00', '80.00', '0.00', '0', '0', '0', '历史古迹,人文景观,晋文化', '1', '80', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('9', '汾河公园', '1', null, '依托汾河太原城区段综合治理而成的滨水公园，是太原的城市名片', null, null, null, '山西省太原市迎泽区滨河东路', '112.5689200', '37.8712500', '全天开放', '0.00', '0.00', '1', '0', '0', '自然风光,休闲,健身', '1', '79', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('10', '双塔寺新馆', '3', null, '太原的地标性建筑，又称永祚寺，以双塔凌霄的景观闻名', null, 'https://images.pexels.com/photos/13009050/pexels-photo-13009050.jpeg', null, '山西省太原市迎泽区郝庄镇郝庄村', '112.5801200', '37.8439800', '08:00-17:30', '30.00', '4.50', '17', '2', '1', '历史古迹,佛教文化,地标', '1', '97', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('11', '蒙山大佛南区', '3', null, '世界第二大石刻佛像，高约66米，始建于北齐天保年间', null, null, null, '山西省太原市晋源区罗城街道寺底村', '112.3978500', '37.7812300', '08:00-17:30', '70.00', '0.00', '4', '0', '0', '自然风光,历史古迹,佛教文化', '1', '94', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('12', '太原植物园', '1', null, '集科学研究、科普教育、园艺展示和文化旅游于一体的综合性植物园', null, null, null, '山西省太原市晋源区晋源街道', '112.4123500', '37.7789200', '09:00-17:00', '50.00', '0.00', '1', '0', '0', '自然风光,科普,亲子', '1', '47', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('13', '青龙古镇新馆', '3', null, '明清时期的商贸重镇，保存有完整的明清建筑群', null, null, null, '山西省太原市阳曲县侯村乡青龙镇村', '112.6897500', '38.0123600', '09:00-18:00', '20.00', '0.00', '0', '0', '0', '历史古迹,民俗,休闲', '1', '13', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('14', '汾河公园新馆', '1', null, '依托汾河太原城区段综合治理而成的滨水公园，是太原的城市名片', null, null, null, '山西省太原市迎泽区滨河东路', '112.5689200', '37.8712500', '全天开放', '0.00', '0.00', '0', '0', '0', '自然风光,休闲,健身', '1', '61', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('15', '钟楼街北区', '4', null, '太原历史最悠久的商业街，融合了传统与现代的商业文化', null, null, null, '山西省太原市迎泽区钟楼街', '112.5489300', '37.8756200', '全天开放', '0.00', '0.00', '0', '0', '0', '现代建筑,商业,美食', '1', '53', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('16', '晋祠博物馆新馆', '4', null, '中国现存最早的皇家祭祀园林，集中国古代祭祀建筑、园林、雕塑、壁画、碑刻艺术为一体', null, null, null, '山西省太原市晋源区晋祠镇', '112.4522300', '37.7964500', '08:00-18:00', '80.00', '0.00', '5', '0', '0', '历史古迹,人文景观,晋文化', '1', '96', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('17', '青龙古镇', '3', null, '明清时期的商贸重镇，保存有完整的明清建筑群', null, null, null, '山西省太原市阳曲县侯村乡青龙镇村', '112.6897500', '38.0123600', '09:00-18:00', '20.00', '0.00', '1', '0', '0', '历史古迹,民俗,休闲', '1', '93', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('18', '太原动物园', '1', null, '集野生动物饲养、展出、繁殖、科研、科普教育为一体的综合性动物园', null, null, null, '山西省太原市杏花岭区东山马路2号', '112.6156700', '37.8987600', '08:00-17:00', '16.00', '0.00', '1', '0', '0', '自然风光,亲子,休闲', '1', '11', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('19', '晋祠博物馆老馆', '2', null, '中国现存最早的皇家祭祀园林，集中国古代祭祀建筑、园林、雕塑、壁画、碑刻艺术为一体', null, null, null, '山西省太原市晋源区晋祠镇', '112.4522300', '37.7964500', '08:00-18:00', '80.00', '0.00', '0', '0', '0', '历史古迹,人文景观,晋文化', '1', '96', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('20', '汾河公园北区', '1', null, '依托汾河太原城区段综合治理而成的滨水公园，是太原的城市名片', null, null, null, '山西省太原市迎泽区滨河东路', '112.5689200', '37.8712500', '全天开放', '0.00', '0.00', '0', '0', '0', '自然风光,休闲,健身', '1', '51', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('21', '双塔寺北区', '3', null, '太原的地标性建筑，又称永祚寺，以双塔凌霄的景观闻名', null, null, null, '山西省太原市迎泽区郝庄镇郝庄村', '112.5801200', '37.8439800', '08:00-17:30', '30.00', '0.00', '0', '0', '0', '历史古迹,佛教文化,地标', '1', '83', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('22', '钟楼街', '4', null, '太原历史最悠久的商业街，融合了传统与现代的商业文化', null, null, null, '山西省太原市迎泽区钟楼街', '112.5489300', '37.8756200', '全天开放', '0.00', '0.00', '0', '0', '0', '现代建筑,商业,美食', '1', '32', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('23', '太原植物园南区', '1', null, '集科学研究、科普教育、园艺展示和文化旅游于一体的综合性植物园', null, null, null, '山西省太原市晋源区晋源街道', '112.4123500', '37.7789200', '09:00-17:00', '50.00', '0.00', '0', '0', '0', '自然风光,科普,亲子', '1', '61', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('24', '蒙山大佛新馆', '3', null, '世界第二大石刻佛像，高约66米，始建于北齐天保年间', null, null, null, '山西省太原市晋源区罗城街道寺底村', '112.3978500', '37.7812300', '08:00-17:30', '70.00', '0.00', '2', '0', '0', '自然风光,历史古迹,佛教文化', '1', '75', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('25', '钟楼街北区', '4', null, '太原历史最悠久的商业街，融合了传统与现代的商业文化', null, null, null, '山西省太原市迎泽区钟楼街', '112.5489300', '37.8756200', '全天开放', '0.00', '0.00', '0', '0', '0', '现代建筑,商业,美食', '1', '14', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('26', '太原植物园南区', '1', null, '集科学研究、科普教育、园艺展示和文化旅游于一体的综合性植物园', null, null, null, '山西省太原市晋源区晋源街道', '112.4123500', '37.7789200', '09:00-17:00', '50.00', '0.00', '0', '0', '0', '自然风光,科普,亲子', '1', '71', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `attraction` VALUES ('27', '测试', null, '6', '测试', '', '', null, null, null, null, '', '0.00', '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 13:54:25', '2026-01-26 13:54:25');
INSERT INTO `attraction` VALUES ('28', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', null, '6', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', '', '', null, null, null, null, '', '0.00', '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:00:51', '2026-01-26 14:00:51');
INSERT INTO `attraction` VALUES ('29', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', null, '6', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', '', '', null, null, null, null, '', '0.00', '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:02:15', '2026-01-26 14:02:15');
INSERT INTO `attraction` VALUES ('30', '12', null, '6', '12', '12', '', null, null, null, null, '', '0.00', '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:06:24', '2026-01-26 14:06:24');
INSERT INTO `attraction` VALUES ('31', '提交美食、文化、攻略时不会再出现 indexOf 错误', null, '6', '提交美食、文化、攻略时不会再出现 indexOf 错误', '', '', null, null, null, null, '', '0.00', '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:09:32', '2026-01-26 14:09:32');
INSERT INTO `attraction` VALUES ('32', '1111', null, '6', 'index.js??clonedRule…=script&lang=js:179 提交美食失败: TypeError: Cannot read properties of undefined (reading \'indexOf\')\n    at VueComponent.resetField (element-ui.common.js:2740:3269)\n    at eval (element-ui.common.js:2684:464)\n    at Array.forEach (<anonymous>)\n    at VueComponent.resetFields (element-ui.common.js:2684:434)\n    at VueComponent.resetFoodForm (index.js??clonedRule…ript&lang=js:542:28)\n    at VueComponent.submitFood (index.js??clonedRule…ript&lang=js:174:16)\n﻿\n', '', '', null, null, null, null, '', '0.00', '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:12:35', '2026-01-26 14:12:35');
INSERT INTO `attraction` VALUES ('33', '晋祠', '3', null, '中国现存最早的皇家祭祀园林，晋国宗祠，有3000多年历史', '中国现存最早的皇家祭祀园林，晋国宗祠，有3000多年历史\n\n中国现存最早的皇家祭祀园林，晋国宗祠，有3000多年历史是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '', null, '山西省太原市晋源区晋祠', '112.4442710', '37.6976700', '08:00-18:00', '80.00', '4.20', '849', '50', '140', '历史,文化,古建筑', '1', '54', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('34', '双塔寺', '3', null, '太原标志性建筑，明代古塔，登塔可俯瞰全城', '太原标志性建筑，明代古塔，登塔可俯瞰全城\n\n太原标志性建筑，明代古塔，登塔可俯瞰全城是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_2.jpg', null, '山西省太原市迎泽区双塔寺街', '112.5680000', '37.8570000', '08:30-17:30', '30.00', '4.70', '4572', '95', '120', '古建筑,登高,历史', '1', '100', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('35', '太原古县城', '3', null, '明代古城，保存完好的古建筑群，体验古代市井文化', '明代古城，保存完好的古建筑群，体验古代市井文化\n\n明代古城，保存完好的古建筑群，体验古代市井文化是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_3.jpg', null, '山西省太原市晋源区', '112.4780000', '37.7150000', '09:00-21:00', '50.00', '4.60', '4097', '107', '28', '古城,文化,历史', '1', '89', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('36', '蒙山大佛', '4', null, '世界第二大石佛，开凿于北齐，高约66米', '世界第二大石佛，开凿于北齐，高约66米\n\n世界第二大石佛，开凿于北齐，高约66米是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_4.jpg', null, '山西省太原市晋源区', '112.4560000', '37.7320000', '08:00-18:00', '70.00', '5.00', '4246', '90', '111', '佛教,历史,自然', '1', '15', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('37', '汾河公园', '2', null, '城市生态公园，沿汾河而建，适合休闲散步', '城市生态公园，沿汾河而建，适合休闲散步\n\n城市生态公园，沿汾河而建，适合休闲散步是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_5.jpg', null, '山西省太原市汾河两岸', '112.5490000', '37.8700000', '全天开放', '0.00', '4.80', '737', '35', '85', '公园,休闲,自然', '1', '31', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('38', '山西博物院', '2', null, '国家一级博物馆，展示山西历史文化', '国家一级博物馆，展示山西历史文化\n\n国家一级博物馆，展示山西历史文化是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_6.jpg', null, '山西省太原市万柏林区滨河西路', '112.5200000', '37.8620000', '09:00-17:00', '0.00', '4.30', '157', '71', '28', '博物馆,文化,历史', '1', '5', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('39', '迎泽公园', '2', null, '太原市中心最大的公园，有湖泊和古建筑', '太原市中心最大的公园，有湖泊和古建筑\n\n太原市中心最大的公园，有湖泊和古建筑是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_7.jpg', null, '山西省太原市迎泽区迎泽大街', '112.5620000', '37.8600000', '06:00-22:00', '0.00', '4.90', '317', '44', '14', '公园,休闲,自然', '1', '46', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('40', '天龙山石窟', '1', null, '北朝至唐代石窟艺术，佛教造像精美', '北朝至唐代石窟艺术，佛教造像精美\n\n北朝至唐代石窟艺术，佛教造像精美是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_8.jpg', null, '山西省太原市晋源区', '112.4320000', '37.7480000', '08:00-17:00', '50.00', '4.80', '3998', '150', '14', '石窟,佛教,历史', '1', '88', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('41', '太山', '3', null, '太原名山，有古寺和自然风光', '太原名山，有古寺和自然风光\n\n太原名山，有古寺和自然风光是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_9.jpg', null, '山西省太原市晋源区', '112.4450000', '37.7250000', '08:00-18:00', '25.00', '4.20', '3876', '72', '56', '山,自然,古寺', '1', '75', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('42', '青龙古镇', '4', null, '明清古镇，保存完好的古建筑群', '明清古镇，保存完好的古建筑群\n\n明清古镇，保存完好的古建筑群是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_10.jpg', null, '山西省太原市阳曲县', '112.6780000', '38.0580000', '09:00-18:00', '60.00', '4.50', '4505', '54', '60', '古镇,文化,历史', '1', '67', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('43', '中国煤炭博物馆', '2', null, '全国唯一煤炭行业博物馆，了解煤炭文化', '全国唯一煤炭行业博物馆，了解煤炭文化\n\n全国唯一煤炭行业博物馆，了解煤炭文化是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_11.jpg', null, '山西省太原市万柏林区', '112.5150000', '37.8550000', '09:00-17:00', '60.00', '4.30', '2011', '75', '118', '博物馆,工业,文化', '1', '24', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('44', '东湖醋园', '2', null, '老陈醋文化园，体验传统制醋工艺', '老陈醋文化园，体验传统制醋工艺\n\n老陈醋文化园，体验传统制醋工艺是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_12.jpg', null, '山西省太原市杏花岭区', '112.5780000', '37.8880000', '08:30-17:30', '20.00', '4.60', '3076', '93', '147', '文化,体验,传统', '1', '69', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('45', '太原植物园', '4', null, '现代化植物园，展示各种植物和花卉', '现代化植物园，展示各种植物和花卉\n\n现代化植物园，展示各种植物和花卉是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_13.jpg', null, '山西省太原市晋源区', '112.4850000', '37.6950000', '09:00-18:00', '50.00', '4.60', '1097', '130', '127', '植物园,自然,休闲', '1', '95', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('46', '纯阳宫', '4', null, '道教宫观，元代建筑，有精美的道教造像', '道教宫观，元代建筑，有精美的道教造像\n\n道教宫观，元代建筑，有精美的道教造像是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_14.jpg', null, '山西省太原市迎泽区', '112.5650000', '37.8630000', '08:30-17:30', '30.00', '4.50', '3115', '163', '35', '道教,古建筑,历史', '1', '38', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('47', '崛围山', '1', null, '太原名山，有古寺和红叶，秋季最美', '太原名山，有古寺和红叶，秋季最美\n\n太原名山，有古寺和红叶，秋季最美是太原市著名的旅游景点，具有深厚的历史文化底蕴。', '/uploads/images/attraction_15.jpg', null, '山西省太原市尖草坪区', '112.5120000', '37.9450000', '08:00-18:00', '20.00', '4.40', '4381', '18', '149', '山,自然,古寺', '1', '33', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `attraction` VALUES ('48', '启春阁', '1', null, '', '', 'https://images.pexels.com/photos/12274675/pexels-photo-12274675.jpeg', null, '太原市万柏林区启春阁', '112.5156380', '37.8597380', '', '0.00', '0.00', '1', '0', '0', null, '1', '0', '2026-02-06 15:40:46', '2026-02-06 15:40:46');
INSERT INTO `attraction` VALUES ('49', ' 黄坡烈士陵园', '3', null, '', '', '', null, '长风西街德育路', '112.4920060', '37.8249600', '', '0.00', '0.00', '1', '0', '0', null, '1', '0', '2026-03-18 20:32:31', '2026-03-18 20:32:31');

-- ----------------------------
-- Table structure for `attraction_category`
-- ----------------------------
DROP TABLE IF EXISTS `attraction_category`;
CREATE TABLE `attraction_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类ID，0表示顶级分类',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='景点分类表';

-- ----------------------------
-- Records of attraction_category
-- ----------------------------
INSERT INTO `attraction_category` VALUES ('1', '自然风光', '0', null, '1', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `attraction_category` VALUES ('2', '人文景观', '0', null, '2', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `attraction_category` VALUES ('3', '历史古迹', '0', null, '3', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `attraction_category` VALUES ('4', '现代建筑', '0', null, '4', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');

-- ----------------------------
-- Table structure for `attraction_image`
-- ----------------------------
DROP TABLE IF EXISTS `attraction_image`;
CREATE TABLE `attraction_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `attraction_id` bigint(20) NOT NULL COMMENT '景点ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `image_type` varchar(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，detail-详情',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_attraction_id` (`attraction_id`),
  CONSTRAINT `fk_attraction_image` FOREIGN KEY (`attraction_id`) REFERENCES `attraction` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb4 COMMENT='景点图片表';

-- ----------------------------
-- Records of attraction_image
-- ----------------------------
INSERT INTO `attraction_image` VALUES ('8', '4', 'https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg', 'cover', '1', '2026-01-09 15:18:35');
INSERT INTO `attraction_image` VALUES ('9', '3', 'https://images.pexels.com/photos/33713697/pexels-photo-33713697.jpeg', 'cover', '1', '2026-01-09 15:40:19');
INSERT INTO `attraction_image` VALUES ('10', '3', 'https://images.pexels.com/photos/31894060/pexels-photo-31894060.jpeg', 'detail', '2', '2026-01-09 15:40:19');
INSERT INTO `attraction_image` VALUES ('13', '10', 'https://images.pexels.com/photos/13009050/pexels-photo-13009050.jpeg', 'cover', '1', '2026-01-23 16:34:59');
INSERT INTO `attraction_image` VALUES ('14', '48', 'https://images.pexels.com/photos/12274675/pexels-photo-12274675.jpeg', 'cover', '1', '2026-02-06 15:40:46');

-- ----------------------------
-- Table structure for `browse_history`
-- ----------------------------
DROP TABLE IF EXISTS `browse_history`;
CREATE TABLE `browse_history` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '记录ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `target_type` varchar(20) NOT NULL COMMENT '浏览对象类型',
  `target_id` bigint(20) NOT NULL COMMENT '浏览对象ID',
  `browse_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '浏览时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_type`,`target_id`),
  KEY `idx_browse_time` (`browse_time`),
  CONSTRAINT `fk_browse_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=84 DEFAULT CHARSET=utf8mb4 COMMENT='用户浏览记录表';

-- ----------------------------
-- Records of browse_history
-- ----------------------------
INSERT INTO `browse_history` VALUES ('1', '3', 'experience', '2', '2026-01-05 11:42:54');
INSERT INTO `browse_history` VALUES ('2', '3', 'experience', '3', '2026-01-05 14:39:21');
INSERT INTO `browse_history` VALUES ('3', '3', 'experience', '1', '2026-01-05 15:54:51');
INSERT INTO `browse_history` VALUES ('4', '3', 'attraction', '1', '2026-01-04 17:05:10');
INSERT INTO `browse_history` VALUES ('5', '3', 'hotel', '1', '2026-01-05 13:17:33');
INSERT INTO `browse_history` VALUES ('6', '3', 'culture', '1', '2026-01-09 16:47:45');
INSERT INTO `browse_history` VALUES ('12', '3', 'hotel', '2', '2026-03-18 20:42:24');
INSERT INTO `browse_history` VALUES ('13', '3', 'attraction', '2', '2026-01-05 15:32:54');
INSERT INTO `browse_history` VALUES ('14', '3', 'food', '1', '2026-01-26 13:38:37');
INSERT INTO `browse_history` VALUES ('16', '3', 'attraction', '3', '2026-01-09 15:39:47');
INSERT INTO `browse_history` VALUES ('18', '3', 'culture', '2', '2026-01-09 16:53:06');
INSERT INTO `browse_history` VALUES ('19', '3', 'strategy', '1', '2026-01-27 14:43:35');
INSERT INTO `browse_history` VALUES ('25', '3', 'attraction', '4', '2026-01-09 15:22:59');
INSERT INTO `browse_history` VALUES ('26', '3', 'attraction', '5', '2026-01-09 17:14:38');
INSERT INTO `browse_history` VALUES ('27', '3', 'hotel', '4', '2026-01-27 15:01:41');
INSERT INTO `browse_history` VALUES ('28', '3', 'hotel', '5', '2026-01-27 15:04:34');
INSERT INTO `browse_history` VALUES ('29', '3', 'attraction', '10', '2026-03-07 15:15:06');
INSERT INTO `browse_history` VALUES ('33', '3', 'hotel', '13', '2026-01-22 10:59:28');
INSERT INTO `browse_history` VALUES ('34', '3', 'culture', '13', '2026-01-22 11:45:59');
INSERT INTO `browse_history` VALUES ('35', '3', 'attraction', '17', '2026-01-22 14:25:59');
INSERT INTO `browse_history` VALUES ('36', '3', 'attraction', '24', '2026-01-23 16:13:29');
INSERT INTO `browse_history` VALUES ('37', '3', 'attraction', '9', '2026-01-23 16:29:47');
INSERT INTO `browse_history` VALUES ('38', '3', 'attraction', '16', '2026-01-27 10:09:41');
INSERT INTO `browse_history` VALUES ('39', '3', 'attraction', '11', '2026-01-23 16:35:04');
INSERT INTO `browse_history` VALUES ('40', '3', 'attraction', '18', '2026-01-23 16:38:07');
INSERT INTO `browse_history` VALUES ('41', '3', 'strategy', '2', '2026-01-27 10:09:30');
INSERT INTO `browse_history` VALUES ('42', '3', 'culture', '9', '2026-01-23 17:19:45');
INSERT INTO `browse_history` VALUES ('44', '3', 'attraction', '12', '2026-01-26 13:42:21');
INSERT INTO `browse_history` VALUES ('45', '6', 'attraction', '3', '2026-01-26 14:21:17');
INSERT INTO `browse_history` VALUES ('46', '3', 'culture', '20', '2026-01-26 14:23:57');
INSERT INTO `browse_history` VALUES ('47', '6', 'food', '7', '2026-01-27 10:00:07');
INSERT INTO `browse_history` VALUES ('48', '3', 'food', '7', '2026-01-28 15:25:07');
INSERT INTO `browse_history` VALUES ('49', '3', 'strategy', '9', '2026-01-27 14:50:24');
INSERT INTO `browse_history` VALUES ('50', '3', 'hotel', '6', '2026-01-27 15:04:28');
INSERT INTO `browse_history` VALUES ('51', '3', 'hotel', '8', '2026-01-27 15:04:35');
INSERT INTO `browse_history` VALUES ('52', '3', 'hotel', '10', '2026-01-28 15:34:36');
INSERT INTO `browse_history` VALUES ('53', '3', 'hotel', '21', '2026-01-28 15:34:46');
INSERT INTO `browse_history` VALUES ('54', '3', 'experience', '6', '2026-01-28 15:35:20');
INSERT INTO `browse_history` VALUES ('55', '3', 'experience', '7', '2026-01-28 15:35:23');
INSERT INTO `browse_history` VALUES ('56', '3', 'experience', '8', '2026-01-28 15:35:26');
INSERT INTO `browse_history` VALUES ('57', '3', 'experience', '12', '2026-01-28 15:35:31');
INSERT INTO `browse_history` VALUES ('58', '3', 'food', '9', '2026-02-06 16:45:00');
INSERT INTO `browse_history` VALUES ('59', '3', 'strategy', '17', '2026-01-28 15:52:35');
INSERT INTO `browse_history` VALUES ('60', '3', 'hotel', '28', '2026-01-28 16:12:56');
INSERT INTO `browse_history` VALUES ('61', '3', 'hotel', '29', '2026-01-28 16:17:31');
INSERT INTO `browse_history` VALUES ('62', '3', 'strategy', '16', '2026-01-28 16:20:57');
INSERT INTO `browse_history` VALUES ('63', '3', 'attraction', '34', '2026-02-02 09:44:32');
INSERT INTO `browse_history` VALUES ('64', '3', 'attraction', '40', '2026-02-02 09:44:40');
INSERT INTO `browse_history` VALUES ('65', '3', 'attraction', '45', '2026-02-02 09:44:44');
INSERT INTO `browse_history` VALUES ('66', '3', 'attraction', '46', '2026-02-02 09:44:50');
INSERT INTO `browse_history` VALUES ('67', '3', 'attraction', '47', '2026-02-02 09:44:55');
INSERT INTO `browse_history` VALUES ('68', '3', 'attraction', '35', '2026-02-02 09:44:58');
INSERT INTO `browse_history` VALUES ('69', '3', 'food', '19', '2026-02-02 09:50:49');
INSERT INTO `browse_history` VALUES ('70', '3', 'attraction', '33', '2026-02-02 09:53:32');
INSERT INTO `browse_history` VALUES ('71', '3', 'strategy', '15', '2026-02-02 10:02:10');
INSERT INTO `browse_history` VALUES ('72', '3', 'hotel', '30', '2026-02-02 10:02:41');
INSERT INTO `browse_history` VALUES ('73', '3', 'hotel', '26', '2026-02-02 10:02:52');
INSERT INTO `browse_history` VALUES ('74', '3', 'experience', '4', '2026-02-02 10:09:10');
INSERT INTO `browse_history` VALUES ('75', '3', 'hotel', '27', '2026-02-02 10:10:11');
INSERT INTO `browse_history` VALUES ('76', '3', 'attraction', '48', '2026-02-06 15:41:28');
INSERT INTO `browse_history` VALUES ('77', '3', 'food', '15', '2026-02-06 16:46:41');
INSERT INTO `browse_history` VALUES ('78', '3', 'attraction', '49', '2026-03-18 20:33:19');
INSERT INTO `browse_history` VALUES ('79', '3', 'strategy', '20', '2026-03-18 20:35:30');
INSERT INTO `browse_history` VALUES ('80', '3', 'strategy', '21', '2026-03-18 20:37:16');
INSERT INTO `browse_history` VALUES ('81', '3', 'hotel', '23', '2026-03-18 20:42:46');
INSERT INTO `browse_history` VALUES ('82', '7', 'hotel', '31', '2026-03-18 20:43:21');
INSERT INTO `browse_history` VALUES ('83', '7', 'hotel', '23', '2026-03-18 20:43:25');

-- ----------------------------
-- Table structure for `comment`
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '评论ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `target_type` varchar(20) NOT NULL COMMENT '评论对象类型：attraction-景点，food-美食，hotel-酒店，culture-文化',
  `target_id` bigint(20) NOT NULL COMMENT '评论对象ID',
  `content` text NOT NULL COMMENT '评论内容',
  `rating` decimal(3,2) DEFAULT NULL COMMENT '评分（0-5）',
  `rating_detail` varchar(500) DEFAULT NULL COMMENT '多维度评分（JSON格式：环境、服务、性价比等）',
  `images` varchar(1000) DEFAULT NULL COMMENT '评论图片（JSON数组）',
  `like_count` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `reply_count` int(11) NOT NULL DEFAULT '0' COMMENT '回复数',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-待审核，1-已通过，2-已拒绝',
  `is_top` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否置顶：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_type`,`target_id`),
  KEY `idx_status` (`status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_comment_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES ('1', '3', 'experience', '3', '111', '5.00', null, null, '0', '0', '1', '0', '2026-01-04 16:33:09', '2026-01-04 16:33:09');
INSERT INTO `comment` VALUES ('2', '3', 'experience', '3', '1111', '1.00', null, null, '0', '0', '1', '0', '2026-01-04 16:33:24', '2026-01-04 16:33:24');
INSERT INTO `comment` VALUES ('3', '3', 'experience', '3', '1231312312', '5.00', null, null, '0', '0', '1', '0', '2026-01-05 10:16:30', '2026-01-05 10:16:30');
INSERT INTO `comment` VALUES ('6', '3', 'hotel', '2', '挺好的', '5.00', null, null, '0', '0', '1', '0', '2026-01-05 13:25:09', '2026-01-05 13:25:09');
INSERT INTO `comment` VALUES ('7', '3', 'hotel', '2', '一般 不是很值', '5.00', null, null, '0', '0', '1', '0', '2026-01-05 13:25:27', '2026-01-05 13:25:27');
INSERT INTO `comment` VALUES ('8', '3', 'hotel', '2', '还可以吧', '2.00', null, null, '0', '0', '1', '0', '2026-01-05 13:25:36', '2026-01-05 13:25:36');
INSERT INTO `comment` VALUES ('9', '3', 'hotel', '23', '测试评论', '1.00', null, null, '0', '0', '1', '0', '2026-03-18 20:42:45', '2026-03-18 20:42:45');

-- ----------------------------
-- Table structure for `comment_reply`
-- ----------------------------
DROP TABLE IF EXISTS `comment_reply`;
CREATE TABLE `comment_reply` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '回复ID',
  `comment_id` bigint(20) NOT NULL COMMENT '评论ID',
  `user_id` bigint(20) NOT NULL COMMENT '回复用户ID',
  `reply_to_id` bigint(20) DEFAULT NULL COMMENT '回复的回复ID（二级回复）',
  `content` text NOT NULL COMMENT '回复内容',
  `status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '状态：0-待审核，1-已通过，2-已拒绝',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_comment_id` (`comment_id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_reply_comment` FOREIGN KEY (`comment_id`) REFERENCES `comment` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_reply_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论回复表';

-- ----------------------------
-- Records of comment_reply
-- ----------------------------

-- ----------------------------
-- Table structure for `culture`
-- ----------------------------
DROP TABLE IF EXISTS `culture`;
CREATE TABLE `culture` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文化ID',
  `name` varchar(100) NOT NULL COMMENT '文化名称',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建用户ID（可为空，系统创建）',
  `description` text COMMENT '文化描述',
  `content` longtext COMMENT '详细内容',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `history` text COMMENT '历史背景',
  `activity_time` varchar(100) DEFAULT NULL COMMENT '活动时间',
  `activity_location` varchar(255) DEFAULT NULL COMMENT '活动地点',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '活动地点经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '活动地点纬度',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '评分（0-5）',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `favorite_count` int(11) NOT NULL DEFAULT '0' COMMENT '收藏数',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_culture_category` FOREIGN KEY (`category_id`) REFERENCES `culture_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COMMENT='民俗文化表';

-- ----------------------------
-- Records of culture
-- ----------------------------
INSERT INTO `culture` VALUES ('1', 'uuuu', null, null, '@所有人 \n各位同学：\n2026年全国硕士研究生招生考试将于2025年12月20日-21日举行，为了全力做好研究生考试工作，现就安全稳定、诚信考试、注意事项等强调如下：\n⭕️1、请全体考生认真阅读“2026年全国硕士研究生招生考试中北大学考点考场规则及考生注意事项”（详见如下公众号推文）；\n⭕️2、请全体考生认真阅读“中北大学2026年硕士研究生招生考试自命题科目试卷袋拆封和答题纸条形码粘贴说明”（详见如下公众号推文）；\n⭕️3、请全体考生认真阅读“2026年全国硕士研究生招生考试中北大学考点关于考场信息查询及安检入口地点的通知”（详见链接）http://grs.nuc.edu.cn/info/1015/8785.htm\n⭕️4、再次提醒大家务必诚信考试和遵守考试纪律，大家要知道研究生考试作弊行为属于违法行为，所有考生务必严格遵守考试纪律，诚信考试，严禁任何人通过任何形式组织作弊、参与作弊、实施作弊，以免给个人造成人生重大影响。\n⭕️5、大家早做准备，提前完成安检，提前进入考场。安检门开始时间较早，请考生错峰尽早过安检，不要出现拥堵。安检门开通时间7：00-8：00、12：30-13：30，请考生尽量早过安检门，避免临近安检结束人多，时间不足，影响正常考试。\n⭕️6、停课提醒：自12月19日（星期五）下午16：00至21日下午18：00，9#、10#、11#、13#、15#、18#教学楼将实行封闭式管理，期间所有课程停上。详见“关于全国硕士研究生招生考试停课通知”https://jwc.nuc.edu.cn/info/1087/11794.htm', '@所有人 \n各位同学：\n2026年全国硕士研究生招生考试将于2025年12月20日-21日举行，为了全力做好研究生考试工作，现就安全稳定、诚信考试、注意事项等强调如下：\n⭕️1、请全体考生认真阅读“2026年全国硕士研究生招生考试中北大学考点考场规则及考生注意事项”（详见如下公众号推文）；\n⭕️2、请全体考生认真阅读“中北大学2026年硕士研究生招生考试自命题科目试卷袋拆封和答题纸条形码粘贴说明”（详见如下公众号推文）；\n⭕️3、请全体考生认真阅读“2026年全国硕士研究生招生考试中北大学考点关于考场信息查询及安检入口地点的通知”（详见链接）http://grs.nuc.edu.cn/info/1015/8785.htm\n⭕️4、再次提醒大家务必诚信考试和遵守考试纪律，大家要知道研究生考试作弊行为属于违法行为，所有考生务必严格遵守考试纪律，诚信考试，严禁任何人通过任何形式组织作弊、参与作弊、实施作弊，以免给个人造成人生重大影响。\n⭕️5、大家早做准备，提前完成安检，提前进入考场。安检门开始时间较早，请考生错峰尽早过安检，不要出现拥堵。安检门开通时间7：00-8：00、12：30-13：30，请考生尽量早过安检门，避免临近安检结束人多，时间不足，影响正常考试。\n⭕️6、停课提醒：自12月19日（星期五）下午16：00至21日下午18：00，9#、10#、11#、13#、15#、18#教学楼将实行封闭式管理，期间所有课程停上。详见“关于全国硕士研究生招生考试停课通知”https://jwc.nuc.edu.cn/info/1087/11794.htm', 'https://images.pexels.com/photos/35163027/pexels-photo-35163027.jpeg', null, '', null, '西山大厦', '112.4581240', '37.8537390', '0.00', '15', '0', '0', null, '1', '0', '2026-01-05 10:01:23', '2026-01-09 17:16:22');
INSERT INTO `culture` VALUES ('3', '测试', null, '4', '摩天轮', '', '', null, '', '', '山西省太原市中北大学', null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-09 16:53:52', '2026-01-09 16:53:52');
INSERT INTO `culture` VALUES ('4', '二月二龙抬头体验课', '1', null, '太原传统节日习俗，有理发、吃龙食等习俗', '二月二龙抬头是太原重要的传统节日，民间有“二月二，剃龙头”的习俗，还有吃面条（龙须面）、吃饺子（龙耳）、吃煎饼（龙鳞）等食俗。', null, null, null, '每年农历二月初二', '山西省太原市各区县', '112.5489300', '37.8756200', '0.00', '0', '0', '0', '传统节日,民俗,习俗', '1', '20', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('5', '太原庙会工坊', '2', null, '太原传统民俗活动，春节期间举办，集祭祀、娱乐、购物于一体', '太原庙会历史悠久，以食品街、动物园等地的庙会最为著名，有传统小吃、民间技艺、文艺表演等，是太原人过年的重要习俗。', null, null, null, '每年春节期间（正月初一至十五）', '山西省太原市迎泽区食品街', '112.5512300', '37.8778900', '0.00', '0', '0', '0', '民俗活动,春节,传统', '1', '69', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('6', '太谷饼制作技艺', '3', null, '太原周边太谷的传统糕点制作技艺', '太谷饼是山西传统糕点，制作工艺复杂，口感酥软，香甜可口，是太原及晋中地区的特色食品，其制作技艺已列入非遗名录。', null, null, null, '每天9:00-17:00可参观制作过程', '山西省太原市小店区食品文化街', '112.5898700', '37.8123400', '0.00', '0', '0', '0', '传统技艺,美食,非遗', '1', '57', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('7', '二月二龙抬头体验课', '1', null, '太原传统节日习俗，有理发、吃龙食等习俗', '二月二龙抬头是太原重要的传统节日，民间有“二月二，剃龙头”的习俗，还有吃面条（龙须面）、吃饺子（龙耳）、吃煎饼（龙鳞）等食俗。', null, null, null, '每年农历二月初二', '山西省太原市各区县', '112.5489300', '37.8756200', '0.00', '0', '0', '0', '传统节日,民俗,习俗', '1', '47', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('8', '琉璃烧制技艺展示', '3', null, '太原晋源区的传统琉璃烧制技艺', '太原琉璃烧制技艺历史悠久，晋源区的琉璃制品闻名全国，广泛应用于古建筑装饰，其烧制技艺已列入国家级非物质文化遗产名录。', null, null, null, '工作日可参观（需预约）', '山西省太原市晋源区琉璃厂', '112.4123500', '37.7789200', '0.00', '0', '0', '0', '传统技艺,非遗,手工', '1', '20', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('9', '太谷饼制作技艺表演', '3', null, '太原周边太谷的传统糕点制作技艺', '太谷饼是山西传统糕点，制作工艺复杂，口感酥软，香甜可口，是太原及晋中地区的特色食品，其制作技艺已列入非遗名录。', null, null, null, '每天9:00-17:00可参观制作过程', '山西省太原市小店区食品文化街', '112.5898700', '37.8123400', '0.00', '1', '0', '0', '传统技艺,美食,非遗', '1', '81', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('10', '晋剧表演展示', '4', null, '山西四大梆子之一，又称中路梆子，是太原及晋中地区的主要剧种', '晋剧表演历史悠久，唱腔婉转，表演细腻，代表剧目有《打金枝》《小宴》《卖画劈门》等。太原各大剧院定期有晋剧演出，是体验山西传统文化的重要方式。', null, null, null, '每周五、六 19:30-21:30', '山西省太原市迎泽区山西大剧院', '112.5123400', '37.8178900', '0.00', '0', '0', '0', '戏曲,民间艺术,传统文化', '1', '65', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('11', '琉璃烧制技艺体验课', '3', null, '太原晋源区的传统琉璃烧制技艺', '太原琉璃烧制技艺历史悠久，晋源区的琉璃制品闻名全国，广泛应用于古建筑装饰，其烧制技艺已列入国家级非物质文化遗产名录。', null, null, null, '工作日可参观（需预约）', '山西省太原市晋源区琉璃厂', '112.4123500', '37.7789200', '0.00', '0', '0', '0', '传统技艺,非遗,手工', '1', '42', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('12', '太原锣鼓工坊', '2', null, '山西民间打击乐，气势磅礴，节奏明快', '太原锣鼓是太原地区的传统民间艺术，常用于节日庆典、开业等场合，主要有威风锣鼓、太原锣鼓等流派，是山西民间文化的重要组成部分。', null, null, null, '节假日表演', '山西省太原市晋源区晋祠公园', '112.4522300', '37.7964500', '0.00', '0', '0', '0', '民俗活动,音乐,传统', '1', '73', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('14', '太原婚俗工坊', '2', null, '太原地区的传统婚嫁习俗，流程丰富，特色鲜明', '太原传统婚俗包括提亲、定亲、迎娶等环节，有拜天地、闹洞房、回门等习俗，保留了许多晋地特色的婚嫁礼仪。', null, null, null, '民俗展示常年开放', '山西省太原市阳曲县民俗村', '112.6897500', '38.0123600', '0.00', '0', '0', '0', '民俗活动,婚嫁,传统', '1', '9', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('15', '剪纸艺术', '3', null, '太原传统民间剪纸，题材丰富，刀法细腻', '太原剪纸艺术历史悠久，题材多为吉祥图案、戏曲人物、花鸟鱼虫等，是太原民间艺术的瑰宝，已列入非物质文化遗产保护名录。', null, null, null, '常年展览，每月有体验课程', '山西省太原市杏花岭区民俗博物馆', '112.5678900', '37.8876500', '0.00', '0', '0', '0', '传统技艺,民间艺术,非遗', '1', '9', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('16', '太谷饼制作技艺', '3', null, '太原周边太谷的传统糕点制作技艺', '太谷饼是山西传统糕点，制作工艺复杂，口感酥软，香甜可口，是太原及晋中地区的特色食品，其制作技艺已列入非遗名录。', null, null, null, '每天9:00-17:00可参观制作过程', '山西省太原市小店区食品文化街', '112.5898700', '37.8123400', '0.00', '0', '0', '0', '传统技艺,美食,非遗', '1', '9', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('17', '中元节习俗体验课', '1', null, '太原地区中元节的传统习俗', '太原中元节有祭祖、放河灯、烧纸钱等习俗，是缅怀先人的重要节日，体现了太原人慎终追远的传统美德。', null, null, null, '每年农历七月十五', '山西省太原市晋源区汾河景区', '112.5689200', '37.8712500', '0.00', '1', '0', '0', '传统节日,民俗,祭祖', '1', '57', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('18', '二月二龙抬头表演', '1', null, '太原传统节日习俗，有理发、吃龙食等习俗', '二月二龙抬头是太原重要的传统节日，民间有“二月二，剃龙头”的习俗，还有吃面条（龙须面）、吃饺子（龙耳）、吃煎饼（龙鳞）等食俗。', null, null, null, '每年农历二月初二', '山西省太原市各区县', '112.5489300', '37.8756200', '0.00', '0', '0', '0', '传统节日,民俗,习俗', '1', '16', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('19', '太原婚俗', '2', null, '太原地区的传统婚嫁习俗，流程丰富，特色鲜明', '太原传统婚俗包括提亲、定亲、迎娶等环节，有拜天地、闹洞房、回门等习俗，保留了许多晋地特色的婚嫁礼仪。', null, null, null, '民俗展示常年开放', '山西省太原市阳曲县民俗村', '112.6897500', '38.0123600', '0.00', '1', '0', '0', '民俗活动,婚嫁,传统', '1', '87', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('20', '剪纸艺术工坊', '3', null, '太原传统民间剪纸，题材丰富，刀法细腻', '太原剪纸艺术历史悠久，题材多为吉祥图案、戏曲人物、花鸟鱼虫等，是太原民间艺术的瑰宝，已列入非物质文化遗产保护名录。', null, null, null, '常年展览，每月有体验课程', '山西省太原市杏花岭区民俗博物馆', '112.5678900', '37.8876500', '0.00', '1', '0', '0', '传统技艺,民间艺术,非遗', '1', '30', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('21', '面塑技艺体验课', '3', null, '山西传统民间技艺，用面粉制作各种人物、动物造型', '太原面塑技艺精湛，造型生动，色彩鲜艳，不仅是食用的面食，更是精美的艺术品，常用于节日、婚丧嫁娶等场合。', null, null, null, '常年展示，周末有体验活动', '山西省太原市小店区非物质文化遗产馆', '112.5876500', '37.8098700', '0.00', '0', '0', '0', '传统技艺,民间艺术,手工', '1', '51', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('22', '琉璃烧制技艺体验课', '3', null, '太原晋源区的传统琉璃烧制技艺', '太原琉璃烧制技艺历史悠久，晋源区的琉璃制品闻名全国，广泛应用于古建筑装饰，其烧制技艺已列入国家级非物质文化遗产名录。', null, null, null, '工作日可参观（需预约）', '山西省太原市晋源区琉璃厂', '112.4123500', '37.7789200', '0.00', '0', '0', '0', '传统技艺,非遗,手工', '1', '56', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('23', '太谷饼制作技艺', '3', null, '太原周边太谷的传统糕点制作技艺', '太谷饼是山西传统糕点，制作工艺复杂，口感酥软，香甜可口，是太原及晋中地区的特色食品，其制作技艺已列入非遗名录。', null, null, null, '每天9:00-17:00可参观制作过程', '山西省太原市小店区食品文化街', '112.5898700', '37.8123400', '0.00', '0', '0', '0', '传统技艺,美食,非遗', '1', '28', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `culture` VALUES ('24', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', null, '6', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', '', '', null, '', '', null, null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:02:27', '2026-01-26 14:02:27');
INSERT INTO `culture` VALUES ('25', '12', null, '6', '12', '', '', null, '', '', null, null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:06:56', '2026-01-26 14:06:56');
INSERT INTO `culture` VALUES ('26', '1111', null, '6', '111', '', '', null, '', '', null, null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:12:48', '2026-01-26 14:12:48');

-- ----------------------------
-- Table structure for `culture_category`
-- ----------------------------
DROP TABLE IF EXISTS `culture_category`;
CREATE TABLE `culture_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类ID',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='文化分类表';

-- ----------------------------
-- Records of culture_category
-- ----------------------------
INSERT INTO `culture_category` VALUES ('1', '传统节日', '0', null, '1', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `culture_category` VALUES ('2', '民俗活动', '0', null, '2', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `culture_category` VALUES ('3', '传统技艺', '0', null, '3', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `culture_category` VALUES ('4', '民间艺术', '0', null, '4', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');

-- ----------------------------
-- Table structure for `culture_image`
-- ----------------------------
DROP TABLE IF EXISTS `culture_image`;
CREATE TABLE `culture_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `culture_id` bigint(20) NOT NULL COMMENT '文化ID',
  `image_url` varchar(500) NOT NULL COMMENT '图片URL',
  `image_type` varchar(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，detail-详情',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_culture_id` (`culture_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文化图片表';

-- ----------------------------
-- Records of culture_image
-- ----------------------------

-- ----------------------------
-- Table structure for `experience`
-- ----------------------------
DROP TABLE IF EXISTS `experience`;
CREATE TABLE `experience` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '体验项目ID',
  `name` varchar(100) NOT NULL COMMENT '项目名称',
  `description` text COMMENT '项目描述',
  `content` longtext COMMENT '详细内容',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `video_url` varchar(255) DEFAULT NULL COMMENT '视频URL',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `duration` int(11) DEFAULT NULL COMMENT '体验时长（分钟）',
  `price` decimal(10,2) DEFAULT NULL COMMENT '价格',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '评分（0-5）',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `favorite_count` int(11) NOT NULL DEFAULT '0' COMMENT '收藏数',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`),
  FULLTEXT KEY `ft_name_desc` (`name`,`description`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4 COMMENT='特色体验项目表';

-- ----------------------------
-- Records of experience
-- ----------------------------
INSERT INTO `experience` VALUES ('1', '传统手工艺体验', '学习制作传统手工艺品，感受家乡文化魅力', '详细体验内容...', 'https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg', null, '文化街123号', null, null, '120', '150.00', '4.80', '10', '0', '0', '手工艺,文化体验,亲子', '1', '0', '2026-01-04 16:16:07', '2026-01-04 16:16:07');
INSERT INTO `experience` VALUES ('3', '特色美食制作', '亲手制作家乡特色美食，学习传统烹饪技艺', '详细体验内容...', 'https://images.pexels.com/photos/235731/pexels-photo-235731.jpeg', null, '美食街88号', null, null, '180', '200.00', '5.00', '19', '2', '0', '美食,烹饪,体验', '1', '0', '2026-01-04 16:16:07', '2026-01-04 16:16:07');
INSERT INTO `experience` VALUES ('4', '汾河生态骑行体验', '沿着汾河公园骑行，欣赏汾河两岸风光', '提供自行车和安全装备，专业领队带队，全程约20公里，中途有休息和讲解环节。', null, null, '山西省太原市迎泽区汾河公园南内环桥入口', '112.5689200', '37.8712500', '180', '96.51', '4.75', '2', '0', '0', '户外,骑行,休闲', '1', '95', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('5', '晋剧脸谱绘制体验（成人版）', '专业老师指导，亲手绘制晋剧脸谱，了解晋剧文化', '体验课程包括晋剧文化讲解、脸谱绘制技巧教学、亲手绘制脸谱等环节，完成的作品可带走留念。', null, null, '山西省太原市迎泽区山西戏曲职业学院', '112.5567800', '37.8678900', '120', '196.29', '4.90', '0', '0', '0', '文化体验,手工,亲子', '1', '17', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('6', '山西面食制作体验（专业版）', '学习制作刀削面、剔尖等山西特色面食', '由资深面点师教学，从和面到制作成品全程指导，体验山西面食的制作乐趣，制作的面食可现场品尝。', null, null, '山西省太原市小店区面食文化体验馆', '112.5876500', '37.8098700', '150', '178.18', '4.80', '2', '0', '0', '美食体验,手工,亲子', '1', '100', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('7', '太原醋文化体验（成人版）', '参观醋厂，了解山西老陈醋制作工艺，品尝不同年份陈醋', '体验包括醋文化讲解、醋厂参观、陈醋制作工艺展示、不同年份陈醋品尝、手工酿醋体验等。', 'https://images.pexels.com/photos/35221905/pexels-photo-35221905.jpeg', null, '山西省太原市清徐县老陈醋文化园', '112.3456700', '37.6789000', '210', '146.28', '4.70', '1', '0', '0', '文化体验,美食,特色', '1', '56', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('8', '太原醋文化体验（专业版）', '参观醋厂，了解山西老陈醋制作工艺，品尝不同年份陈醋', '体验包括醋文化讲解、醋厂参观、陈醋制作工艺展示、不同年份陈醋品尝、手工酿醋体验等。', null, null, '山西省太原市清徐县老陈醋文化园', '112.3456700', '37.6789000', '210', '161.57', '4.70', '1', '0', '0', '文化体验,美食,特色', '1', '55', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('9', '太原古城墙徒步体验（专业版）', '沿着太原残存的古城墙徒步，了解太原城市历史', '专业领队带队，徒步太原古城墙遗址，讲解太原城市发展历史，欣赏城市风光。', null, null, '山西省太原市迎泽区古城墙遗址公园', '112.5489300', '37.8756200', '150', '94.21', '4.60', '0', '0', '0', '户外,历史,休闲', '1', '24', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('10', '面塑制作体验（亲子版）', '非遗传承人教学，学习制作山西特色面塑', '从和面、调色到造型，全程指导，可制作生肖、花卉等造型，作品可带走，适合亲子体验。', null, null, '山西省太原市杏花岭区民俗体验馆', '112.5678900', '37.8876500', '120', '119.33', '4.85', '0', '0', '0', '手工,非遗,亲子', '1', '36', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('12', '太原古城墙徒步体验（亲子版）', '沿着太原残存的古城墙徒步，了解太原城市历史', '专业领队带队，徒步太原古城墙遗址，讲解太原城市发展历史，欣赏城市风光。', null, null, '山西省太原市迎泽区古城墙遗址公园', '112.5489300', '37.8756200', '150', '89.14', '4.60', '1', '0', '0', '户外,历史,休闲', '1', '23', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('13', '晋祠深度讲解体验（亲子版）', '专业导游深度讲解晋祠历史文化，了解三晋文脉', '3小时深度讲解，包括晋祠建筑、雕塑、碑刻、历史故事等，让您全面了解晋祠的文化价值。', null, null, '山西省太原市晋源区晋祠博物馆', '112.4522300', '37.7964500', '180', '300.72', '4.95', '0', '0', '0', '文化体验,讲解,历史', '1', '2', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('14', '剪纸艺术体验', '非遗传承人教学，学习山西传统剪纸技艺', '从基础剪纸技巧到创作完整作品，老师一对一指导，适合各年龄段体验，作品可带走。', null, null, '山西省太原市小店区非物质文化遗产馆', '112.5876500', '37.8098700', '90', '103.86', '4.80', '0', '0', '0', '手工,非遗,文化', '1', '91', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('15', '太原古城墙徒步体验', '沿着太原残存的古城墙徒步，了解太原城市历史', '专业领队带队，徒步太原古城墙遗址，讲解太原城市发展历史，欣赏城市风光。', null, null, '山西省太原市迎泽区古城墙遗址公园', '112.5489300', '37.8756200', '150', '84.83', '4.60', '0', '0', '0', '户外,历史,休闲', '1', '92', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('16', '剪纸艺术体验', '非遗传承人教学，学习山西传统剪纸技艺', '从基础剪纸技巧到创作完整作品，老师一对一指导，适合各年龄段体验，作品可带走。', null, null, '山西省太原市小店区非物质文化遗产馆', '112.5876500', '37.8098700', '90', '112.47', '4.80', '0', '0', '0', '手工,非遗,文化', '1', '45', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('17', '太原古城墙徒步体验', '沿着太原残存的古城墙徒步，了解太原城市历史', '专业领队带队，徒步太原古城墙遗址，讲解太原城市发展历史，欣赏城市风光。', null, null, '山西省太原市迎泽区古城墙遗址公园', '112.5489300', '37.8756200', '150', '96.44', '4.60', '0', '0', '0', '户外,历史,休闲', '1', '40', '2026-01-19 15:27:53', '2026-01-19 15:27:53');
INSERT INTO `experience` VALUES ('18', '山西面食制作体验', '学习制作刀削面、剔尖等山西特色面食', '由资深面点师教学，从和面到制作成品全程指导，体验山西面食的制作乐趣，制作的面食可现场品尝。', null, null, '山西省太原市小店区面食文化体验馆', '112.5876500', '37.8098700', '150', '175.48', '4.80', '0', '0', '0', '美食体验,手工,亲子', '1', '18', '2026-01-19 15:27:53', '2026-01-19 15:27:53');

-- ----------------------------
-- Table structure for `experience_image`
-- ----------------------------
DROP TABLE IF EXISTS `experience_image`;
CREATE TABLE `experience_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `experience_id` bigint(20) NOT NULL COMMENT '体验项目ID',
  `image_url` varchar(500) NOT NULL COMMENT '图片URL',
  `image_type` varchar(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，detail-详情',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_experience_id` (`experience_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='体验项目图片表';

-- ----------------------------
-- Records of experience_image
-- ----------------------------
INSERT INTO `experience_image` VALUES ('1', '3', 'https://images.pexels.com/photos/618491/pexels-photo-618491.jpeg', 'cover', '1', '2026-01-05 15:54:19');
INSERT INTO `experience_image` VALUES ('2', '1', 'https://images.pexels.com/photos/734102/pexels-photo-734102.jpeg', 'cover', '1', '2026-01-05 15:54:44');
INSERT INTO `experience_image` VALUES ('3', '7', 'https://images.pexels.com/photos/35221905/pexels-photo-35221905.jpeg', 'cover', '1', '2026-01-26 13:47:22');

-- ----------------------------
-- Table structure for `favorite`
-- ----------------------------
DROP TABLE IF EXISTS `favorite`;
CREATE TABLE `favorite` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '收藏ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `target_type` varchar(20) NOT NULL COMMENT '收藏对象类型：attraction-景点，food-美食，strategy-攻略，culture-文化',
  `target_id` bigint(20) NOT NULL COMMENT '收藏对象ID',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_target` (`user_id`,`target_type`,`target_id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_target` (`target_type`,`target_id`),
  CONSTRAINT `fk_favorite_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COMMENT='收藏表';

-- ----------------------------
-- Records of favorite
-- ----------------------------
INSERT INTO `favorite` VALUES ('1', '6', 'food', '7', '2026-01-27 09:59:50');
INSERT INTO `favorite` VALUES ('2', '3', 'food', '9', '2026-02-06 16:44:53');

-- ----------------------------
-- Table structure for `food`
-- ----------------------------
DROP TABLE IF EXISTS `food`;
CREATE TABLE `food` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '美食ID',
  `name` varchar(100) NOT NULL COMMENT '美食名称',
  `category_id` bigint(20) DEFAULT NULL COMMENT '分类ID',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建用户ID（可为空，系统创建）',
  `description` text COMMENT '美食描述',
  `content` longtext COMMENT '详细内容（制作方法等）',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `video_url` varchar(255) DEFAULT NULL COMMENT '制作视频URL',
  `origin` varchar(255) DEFAULT NULL COMMENT '起源/历史',
  `ingredients` text COMMENT '主要食材',
  `cooking_method` text COMMENT '制作方法',
  `recommended_restaurants` varchar(255) DEFAULT NULL COMMENT '推荐餐厅（JSON格式）',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '评分（0-5）',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `favorite_count` int(11) NOT NULL DEFAULT '0' COMMENT '收藏数',
  `tags` varchar(255) DEFAULT NULL COMMENT '标签（逗号分隔）',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_category_id` (`category_id`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`),
  FULLTEXT KEY `ft_name_desc` (`name`,`description`),
  CONSTRAINT `fk_food_category` FOREIGN KEY (`category_id`) REFERENCES `food_category` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4 COMMENT='美食表';

-- ----------------------------
-- Records of food
-- ----------------------------
INSERT INTO `food` VALUES ('3', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', null, '6', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', '', '', null, null, '', null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:02:21', '2026-01-26 14:02:21');
INSERT INTO `food` VALUES ('4', '12', null, '6', '12', '', '', null, null, '', null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:06:28', '2026-01-26 14:06:28');
INSERT INTO `food` VALUES ('5', '提交美食、文化、攻略时不会再出现 indexOf 错误', null, '6', '提交美食、文化、攻略时不会再出现 indexOf 错误', '', '', null, null, '', null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:09:39', '2026-01-26 14:09:39');
INSERT INTO `food` VALUES ('6', '111', null, '6', '111', '', '', null, null, '', null, null, '0.00', '0', '0', '0', null, '2', '0', '2026-01-26 14:12:41', '2026-01-26 14:12:41');
INSERT INTO `food` VALUES ('7', '烧烤', '1', null, '烧烤', '烧烤', 'https://images.pexels.com/photos/16357827/pexels-photo-16357827.jpeg', null, null, '牛羊肉', '烧烤', '[{\"address\":\"太原北大街老刘烧烤\",\"latitude\":37.888072,\"name\":\"老刘烧烤\",\"longitude\":112.561684}]', '0.00', '7', '0', '1', null, '1', '0', '2026-01-26 14:23:36', '2026-01-26 14:23:36');
INSERT INTO `food` VALUES ('8', '刀削面', '4', null, '山西传统面食，面条宽厚，口感劲道', '制作方法：\n1. 准备食材：面粉,水,猪肉,青菜\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_1.jpg', null, '山西传统面食，历史悠久', '面粉,水,猪肉,青菜', '传统制作工艺，需要掌握火候和技巧', null, '4.00', '112', '135', '6', '山西,传统,美食', '1', '49', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('9', '过油肉', '1', null, '山西名菜，肉片滑嫩，配菜丰富', '制作方法：\n1. 准备食材：猪肉,木耳,黄瓜,鸡蛋\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_2.jpg', null, '山西传统名菜', '猪肉,木耳,黄瓜,鸡蛋', '传统制作工艺，需要掌握火候和技巧', null, '4.90', '2684', '33', '72', '山西,传统,美食', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('10', '糖醋里脊', '2', null, '酸甜可口的经典菜品', '制作方法：\n1. 准备食材：猪里脊,糖,醋,番茄酱\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_3.jpg', null, '山西家常菜', '猪里脊,糖,醋,番茄酱', '传统制作工艺，需要掌握火候和技巧', null, '4.50', '2152', '53', '96', '山西,传统,美食', '1', '74', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('11', '老陈醋', '1', null, '山西特产，酸香醇厚，历史悠久', '制作方法：\n1. 准备食材：高粱,大麦,豌豆\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_4.jpg', null, '山西传统特产，有3000年历史', '高粱,大麦,豌豆', '传统制作工艺，需要掌握火候和技巧', null, '4.60', '1393', '150', '64', '山西,传统,美食', '1', '68', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('12', '平遥牛肉', '2', null, '山西名产，肉质鲜嫩，香味浓郁', '制作方法：\n1. 准备食材：牛肉,盐,香料\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_5.jpg', null, '平遥传统名产', '牛肉,盐,香料', '传统制作工艺，需要掌握火候和技巧', null, '4.30', '1104', '31', '99', '山西,传统,美食', '1', '36', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('13', '莜面栲栳栳', '4', null, '山西特色面食，形状独特，口感Q弹', '制作方法：\n1. 准备食材：莜面,水\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_6.jpg', null, '山西传统面食', '莜面,水', '传统制作工艺，需要掌握火候和技巧', null, '4.70', '238', '92', '35', '山西,传统,美食', '1', '19', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('14', '头脑', '1', null, '太原特色早餐，营养丰富', '制作方法：\n1. 准备食材：羊肉,山药,黄酒,面粉\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_7.jpg', null, '太原传统名食', '羊肉,山药,黄酒,面粉', '传统制作工艺，需要掌握火候和技巧', null, '4.40', '1835', '126', '9', '山西,传统,美食', '1', '15', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('15', '羊杂割', '1', null, '山西特色小吃，汤鲜味美', '制作方法：\n1. 准备食材：羊杂,羊汤,香菜\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_8.jpg', null, '山西传统小吃', '羊杂,羊汤,香菜', '传统制作工艺，需要掌握火候和技巧', null, '4.70', '2123', '136', '31', '山西,传统,美食', '1', '80', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('16', '灌肠', '2', null, '山西特色小吃，口感独特', '制作方法：\n1. 准备食材：荞面,水\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_9.jpg', null, '山西传统小吃', '荞面,水', '传统制作工艺，需要掌握火候和技巧', null, '4.50', '2402', '37', '42', '山西,传统,美食', '1', '42', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('17', '猫耳朵', '1', null, '山西传统面食，形状像猫耳朵', '制作方法：\n1. 准备食材：面粉,水,配菜\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_10.jpg', null, '山西传统面食', '面粉,水,配菜', '传统制作工艺，需要掌握火候和技巧', null, '4.50', '2838', '102', '10', '山西,传统,美食', '1', '30', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('19', '拨鱼儿', '3', null, '山西特色面食，形状像小鱼', '制作方法：\n1. 准备食材：面粉,水\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_12.jpg', null, '山西传统面食', '面粉,水', '传统制作工艺，需要掌握火候和技巧', null, '4.40', '2975', '30', '87', '山西,传统,美食', '1', '16', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('20', '碗托', '3', null, '山西特色小吃，口感爽滑', '制作方法：\n1. 准备食材：荞面,水\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_13.jpg', null, '山西传统小吃', '荞面,水', '传统制作工艺，需要掌握火候和技巧', null, '4.60', '278', '79', '22', '山西,传统,美食', '1', '74', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('21', '油糕', '2', null, '山西传统糕点，外酥内软', '制作方法：\n1. 准备食材：黄米面,豆沙,糖\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_14.jpg', null, '山西传统糕点', '黄米面,豆沙,糖', '传统制作工艺，需要掌握火候和技巧', null, '4.10', '860', '31', '25', '山西,传统,美食', '1', '67', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `food` VALUES ('22', '太谷饼', '4', null, '山西名点，香甜可口', '制作方法：\n1. 准备食材：面粉,糖,芝麻\n2. 按照传统工艺制作\n3. 调味后即可食用', '/uploads/images/food_15.jpg', null, '太谷传统名点', '面粉,糖,芝麻', '传统制作工艺，需要掌握火候和技巧', null, '4.80', '2676', '76', '67', '山西,传统,美食', '1', '22', '2026-01-28 15:51:44', '2026-01-28 15:51:44');

-- ----------------------------
-- Table structure for `food_category`
-- ----------------------------
DROP TABLE IF EXISTS `food_category`;
CREATE TABLE `food_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '分类ID',
  `name` varchar(50) NOT NULL COMMENT '分类名称',
  `parent_id` bigint(20) DEFAULT '0' COMMENT '父分类ID',
  `icon` varchar(255) DEFAULT NULL COMMENT '图标URL',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_parent_id` (`parent_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COMMENT='美食分类表';

-- ----------------------------
-- Records of food_category
-- ----------------------------
INSERT INTO `food_category` VALUES ('1', '特色小吃', '0', null, '1', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `food_category` VALUES ('2', '正餐', '0', null, '2', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `food_category` VALUES ('3', '甜品', '0', null, '3', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `food_category` VALUES ('4', '饮品', '0', null, '4', '1', '2026-01-04 10:08:58', '2026-01-04 10:08:58');

-- ----------------------------
-- Table structure for `food_image`
-- ----------------------------
DROP TABLE IF EXISTS `food_image`;
CREATE TABLE `food_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `food_id` bigint(20) NOT NULL COMMENT '美食ID',
  `image_url` varchar(255) NOT NULL COMMENT '图片URL',
  `image_type` varchar(20) DEFAULT 'detail' COMMENT '图片类型',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_food_id` (`food_id`),
  CONSTRAINT `fk_food_image` FOREIGN KEY (`food_id`) REFERENCES `food` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COMMENT='美食图片表';

-- ----------------------------
-- Records of food_image
-- ----------------------------
INSERT INTO `food_image` VALUES ('25', '7', 'https://images.pexels.com/photos/35570027/pexels-photo-35570027.jpeg', 'cover', '1', '2026-01-26 14:23:36');

-- ----------------------------
-- Table structure for `hotel`
-- ----------------------------
DROP TABLE IF EXISTS `hotel`;
CREATE TABLE `hotel` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '酒店ID',
  `name` varchar(100) NOT NULL COMMENT '酒店名称',
  `description` text COMMENT '酒店描述',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `address` varchar(255) DEFAULT NULL COMMENT '地址',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `phone` varchar(20) DEFAULT NULL COMMENT '联系电话',
  `star_level` tinyint(1) DEFAULT NULL COMMENT '星级（1-5）',
  `rating` decimal(3,2) DEFAULT '0.00' COMMENT '评分（0-5）',
  `facilities` varchar(500) DEFAULT NULL COMMENT '设施（JSON格式）',
  `min_price` decimal(10,2) DEFAULT NULL COMMENT '最低价格',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_status` (`status`),
  KEY `idx_rating` (`rating`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COMMENT='酒店表';

-- ----------------------------
-- Records of hotel
-- ----------------------------
INSERT INTO `hotel` VALUES ('2', '山西国贸大饭店', '测试酒店', 'https://images.pexels.com/photos/35199314/pexels-photo-35199314.jpeg', '山西省太原市杏花岭区府西街69号', '112.5573300', '37.8740230', '0351-6578123', '5', '4.00', null, '510.00', '34', '3', '1', '2026-01-05 13:23:50', '2026-01-05 13:23:50');
INSERT INTO `hotel` VALUES ('23', '太原迎泽宾馆', '太原迎泽宾馆位于山西省太原市迎泽区迎泽大街189号，是一家5星级酒店，设施完善，服务优质。', '/uploads/images/hotel_1.jpg', '山西省太原市迎泽区迎泽大街189号', '112.5620000', '37.8600000', '0351-8828888', '5', '4.08', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '500.00', '2241', '172', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('24', '山西饭店', '山西饭店位于山西省太原市迎泽区纯阳宫街21号，是一家4星级酒店，设施完善，服务优质。', '/uploads/images/hotel_2.jpg', '山西省太原市迎泽区纯阳宫街21号', '112.5650000', '37.8630000', '0351-8228888', '4', '4.20', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '500.00', '219', '295', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('25', '太原万达文华酒店', '太原万达文华酒店位于山西省太原市杏花岭区解放路169号，是一家5星级酒店，设施完善，服务优质。', '/uploads/images/hotel_3.jpg', '山西省太原市杏花岭区解放路169号', '112.5780000', '37.8880000', '0351-8888888', '5', '4.90', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '125.00', '1851', '168', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('26', '太原凯宾斯基酒店', '太原凯宾斯基酒店位于山西省太原市小店区长风街113号，是一家5星级酒店，设施完善，服务优质。', '/uploads/images/hotel_4.jpg', '山西省太原市小店区长风街113号', '112.5450000', '37.8250000', '0351-8999999', '5', '4.60', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '502.00', '2156', '166', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('27', '太原万怡酒店', '太原万怡酒店位于山西省太原市万柏林区长风西街1号，是一家4星级酒店，设施完善，服务优质。', '/uploads/images/hotel_5.jpg', '山西省太原市万柏林区长风西街1号', '112.5200000', '37.8620000', '0351-8777777', '4', '4.60', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '400.00', '1869', '81', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('28', '太原丽华大酒店', '太原丽华大酒店位于山西省太原市小店区长风街12号，是一家4星级酒店，设施完善，服务优质。', '/uploads/images/hotel_6.jpg', '山西省太原市小店区长风街12号', '112.5480000', '37.8300000', '0351-8666666', '4', '4.60', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '400.00', '785', '87', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('29', '太原晋祠宾馆', '太原晋祠宾馆位于山西省太原市晋源区晋祠镇，是一家4星级酒店，设施完善，服务优质。', '/uploads/images/hotel_7.jpg', '山西省太原市晋源区晋祠镇', '112.4850000', '37.7070000', '0351-8555555', '4', '4.20', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '400.00', '203', '231', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('30', '太原如家酒店', '太原如家酒店位于山西省太原市迎泽区解放路88号，是一家3星级酒店，设施完善，服务优质。', '/uploads/images/hotel_8.jpg', '山西省太原市迎泽区解放路88号', '112.5700000', '37.8700000', '0351-8444444', '3', '4.40', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '375.00', '4244', '288', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('31', '太原汉庭酒店', '太原汉庭酒店位于山西省太原市小店区平阳路99号，是一家3星级酒店，设施完善，服务优质。', '/uploads/images/hotel_9.jpg', '山西省太原市小店区平阳路99号', '112.5500000', '37.8400000', '0351-8333333', '3', '4.10', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '300.00', '847', '166', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel` VALUES ('32', '太原7天酒店', '太原7天酒店位于山西省太原市万柏林区漪汾街66号，是一家2星级酒店，设施完善，服务优质。', '/uploads/images/hotel_10.jpg', '山西省太原市万柏林区漪汾街66号', '112.5100000', '37.8800000', '0351-8222222', '2', '4.20', '[\"WiFi\", \"停车场\", \"餐厅\", \"会议室\", \"健身房\", \"游泳池\"]', '200.00', '4479', '285', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');

-- ----------------------------
-- Table structure for `hotel_image`
-- ----------------------------
DROP TABLE IF EXISTS `hotel_image`;
CREATE TABLE `hotel_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `hotel_id` bigint(20) NOT NULL COMMENT '酒店ID',
  `image_url` varchar(500) NOT NULL COMMENT '图片URL',
  `image_type` varchar(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，room-房间，facility-设施，environment-环境',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_hotel_id` (`hotel_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COMMENT='酒店图片表';

-- ----------------------------
-- Records of hotel_image
-- ----------------------------
INSERT INTO `hotel_image` VALUES ('3', '2', 'https://images.pexels.com/photos/35199314/pexels-photo-35199314.jpeg', 'cover', '1', '2026-01-09 15:20:58');

-- ----------------------------
-- Table structure for `hotel_room`
-- ----------------------------
DROP TABLE IF EXISTS `hotel_room`;
CREATE TABLE `hotel_room` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '房间ID',
  `hotel_id` bigint(20) NOT NULL COMMENT '酒店ID',
  `room_type` varchar(50) NOT NULL COMMENT '房间类型',
  `room_name` varchar(100) NOT NULL COMMENT '房间名称',
  `description` text COMMENT '房间描述',
  `image` varchar(255) DEFAULT NULL COMMENT '房间图片',
  `area` decimal(5,2) DEFAULT NULL COMMENT '面积（平方米）',
  `bed_type` varchar(50) DEFAULT NULL COMMENT '床型',
  `max_occupancy` int(11) DEFAULT NULL COMMENT '最大入住人数',
  `facilities` varchar(500) DEFAULT NULL COMMENT '房间设施（JSON格式）',
  `price` decimal(10,2) NOT NULL COMMENT '价格',
  `stock` int(11) NOT NULL DEFAULT '0' COMMENT '库存数量',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-下架，1-上架',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_hotel_id` (`hotel_id`),
  KEY `idx_status` (`status`),
  CONSTRAINT `fk_hotel_room` FOREIGN KEY (`hotel_id`) REFERENCES `hotel` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COMMENT='酒店房间表';

-- ----------------------------
-- Records of hotel_room
-- ----------------------------
INSERT INTO `hotel_room` VALUES ('2', '2', '双床房', '豪华双床房', '', '', '45.00', '1.35m单人床*2', '2', null, '690.00', '10', '1', '2026-01-05 13:23:50', '2026-01-05 13:23:50');
INSERT INTO `hotel_room` VALUES ('3', '2', '大床房', '豪华大床房', '', 'https://images.pexels.com/photos/35199314/pexels-photo-35199314.jpeg', '35.00', '2m大床', '2', null, '510.00', '5', '1', '2026-01-05 13:23:50', '2026-01-05 13:23:50');
INSERT INTO `hotel_room` VALUES ('4', '23', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_1_1.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '625.00', '18', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('5', '23', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_1_2.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '500.00', '20', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('6', '23', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_1_3.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '625.00', '15', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('7', '24', '套房', '行政套房', '行政套房，面积60平方米，大床，可住4人', '/uploads/images/room_2_1.jpg', '60.00', '大床', '4', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '1000.00', '14', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('8', '24', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_2_2.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '500.00', '11', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('9', '25', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_3_1.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '500.00', '13', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('10', '25', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_3_2.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '125.00', '7', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('11', '26', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_4_1.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '502.00', '16', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('12', '26', '家庭房', '家庭双床房', '家庭双床房，面积35平方米，双床，可住3人', '/uploads/images/room_4_2.jpg', '35.00', '双床', '3', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '750.00', '11', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('13', '27', '家庭房', '家庭双床房', '家庭双床房，面积35平方米，双床，可住3人', '/uploads/images/room_5_1.jpg', '35.00', '双床', '3', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '600.00', '11', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('14', '27', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_5_2.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '400.00', '14', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('15', '28', '家庭房', '家庭双床房', '家庭双床房，面积35平方米，双床，可住3人', '/uploads/images/room_6_1.jpg', '35.00', '双床', '3', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '600.00', '7', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('16', '28', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_6_2.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '400.00', '17', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('17', '29', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_7_1.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '400.00', '19', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('18', '29', '套房', '行政套房', '行政套房，面积60平方米，大床，可住4人', '/uploads/images/room_7_2.jpg', '60.00', '大床', '4', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '1000.00', '11', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('19', '30', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_8_1.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '375.00', '19', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('20', '30', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_8_2.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '375.00', '9', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('21', '31', '套房', '行政套房', '行政套房，面积60平方米，大床，可住4人', '/uploads/images/room_9_1.jpg', '60.00', '大床', '4', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '750.00', '17', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('22', '31', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_9_2.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '375.00', '17', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('23', '31', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_9_3.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '300.00', '12', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('24', '32', '大床房', '豪华大床房', '豪华大床房，面积30平方米，大床，可住2人', '/uploads/images/room_10_1.jpg', '30.00', '大床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '250.00', '18', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `hotel_room` VALUES ('25', '32', '标准间', '标准双床房', '标准双床房，面积25平方米，双床，可住2人', '/uploads/images/room_10_2.jpg', '25.00', '双床', '2', '[\"WiFi\", \"电视\", \"空调\", \"独立卫浴\", \"免费洗漱用品\"]', '200.00', '10', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单ID',
  `order_no` varchar(50) NOT NULL COMMENT '订单号，唯一',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `order_type` varchar(20) NOT NULL COMMENT '订单类型：ticket-门票，hotel-酒店，experience-体验',
  `total_amount` decimal(10,2) NOT NULL COMMENT '订单总金额',
  `discount_amount` decimal(10,2) DEFAULT '0.00' COMMENT '优惠金额',
  `pay_amount` decimal(10,2) NOT NULL COMMENT '实付金额',
  `pay_method` varchar(20) DEFAULT NULL COMMENT '支付方式：alipay-支付宝，wechat-微信',
  `pay_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态：0-未支付，1-已支付，2-已退款',
  `order_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '订单状态：0-待支付，1-已支付，2-已使用，3-已取消，4-已退款',
  `contact_name` varchar(50) NOT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) NOT NULL COMMENT '联系人电话',
  `remark` varchar(500) DEFAULT NULL COMMENT '备注',
  `pay_time` datetime DEFAULT NULL COMMENT '支付时间',
  `use_time` datetime DEFAULT NULL COMMENT '使用时间',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_order_status` (`order_status`),
  KEY `idx_pay_status` (`pay_status`),
  KEY `idx_create_time` (`create_time`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='订单表';

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('1', '202601041645220001', '3', 'experience', '150.00', '0.00', '150.00', 'alipay', '1', '1', 'admin11', '19722752347', null, '2026-01-04 16:45:29', null, null, '2026-01-04 16:45:23', '2026-01-04 16:45:23');
INSERT INTO `order` VALUES ('2', '202601041645330002', '3', 'experience', '150.00', '0.00', '150.00', null, '0', '3', 'admin11', '19722752347', null, null, null, '2026-01-04 16:45:43', '2026-01-04 16:45:33', '2026-01-04 16:45:33');
INSERT INTO `order` VALUES ('3', '202601051131000001', '3', 'hotel', '100.00', '0.00', '100.00', 'alipay', '1', '1', 'admin11', '19722752347', '入住：2026-01-06，退房：2026-01-22。', '2026-01-05 11:31:03', null, null, '2026-01-05 11:31:00', '2026-01-05 11:31:00');
INSERT INTO `order` VALUES ('8', '202601231638160001', '3', 'ticket', '16.00', '0.00', '16.00', 'alipay', '1', '1', 'admin11', '19722752347', '', '2026-01-23 16:38:22', null, null, '2026-01-23 16:38:17', '2026-01-23 16:38:17');
INSERT INTO `order` VALUES ('9', '202601271009530001', '3', 'ticket', '480.00', '0.00', '480.00', 'alipay', '1', '1', 'admin11', '19722752347', '', '2026-01-27 10:10:05', null, null, '2026-01-27 10:09:53', '2026-01-27 10:09:53');
INSERT INTO `order` VALUES ('10', '202603182042170001', '3', 'hotel', '510.00', '0.00', '510.00', null, '0', '3', 'admin11', '19722752347', '入住：2026-03-11，退房：2026-03-13。', null, null, '2026-03-18 20:42:21', '2026-03-18 20:42:17', '2026-03-18 20:42:17');

-- ----------------------------
-- Table structure for `order_item`
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单项ID',
  `order_id` bigint(20) NOT NULL COMMENT '订单ID',
  `item_type` varchar(20) NOT NULL COMMENT '项目类型：ticket-门票，hotel-酒店，experience-体验',
  `item_id` bigint(20) NOT NULL COMMENT '项目ID（景点ID、酒店ID等）',
  `item_name` varchar(200) NOT NULL COMMENT '项目名称',
  `item_image` varchar(255) DEFAULT NULL COMMENT '项目图片',
  `quantity` int(11) NOT NULL COMMENT '数量',
  `unit_price` decimal(10,2) NOT NULL COMMENT '单价',
  `total_price` decimal(10,2) NOT NULL COMMENT '总价',
  `use_date` date DEFAULT NULL COMMENT '使用日期',
  `use_time` varchar(50) DEFAULT NULL COMMENT '使用时间',
  `extra_info` text COMMENT '额外信息（JSON格式）',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  KEY `idx_item_id` (`item_id`),
  CONSTRAINT `fk_order_item` FOREIGN KEY (`order_id`) REFERENCES `order` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COMMENT='订单项表';

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES ('1', '1', 'experience', '1', '传统手工艺体验', '/uploads/experience/handicraft.jpg', '1', '150.00', '150.00', null, null, null, '2026-01-04 16:45:23');
INSERT INTO `order_item` VALUES ('2', '2', 'experience', '1', '传统手工艺体验', '/uploads/experience/handicraft.jpg', '1', '150.00', '150.00', null, null, null, '2026-01-04 16:45:33');
INSERT INTO `order_item` VALUES ('3', '3', 'hotel', '1', '121-双床房112312', '', '1', '100.00', '100.00', '2026-01-06', null, '{\"roomPrice\":100,\"checkOutDate\":\"2026-01-22\",\"checkInDate\":\"2026-01-06\",\"roomType\":\"双床房\",\"roomName\":\"双床房112312\"}', '2026-01-05 11:31:00');
INSERT INTO `order_item` VALUES ('8', '8', 'ticket', '18', '太原动物园门票', null, '1', '16.00', '16.00', '2026-01-29', null, null, '2026-01-23 16:38:17');
INSERT INTO `order_item` VALUES ('9', '9', 'ticket', '16', '晋祠博物馆新馆门票', null, '6', '80.00', '480.00', '2026-01-29', null, null, '2026-01-27 10:09:53');
INSERT INTO `order_item` VALUES ('10', '10', 'hotel', '3', '山西国贸大饭店-豪华大床房', 'https://images.pexels.com/photos/35199314/pexels-photo-35199314.jpeg', '1', '510.00', '510.00', '2026-03-11', null, '{\"roomPrice\":510,\"checkOutDate\":\"2026-03-13\",\"checkInDate\":\"2026-03-11\",\"roomType\":\"大床房\",\"roomName\":\"豪华大床房\"}', '2026-03-18 20:42:17');

-- ----------------------------
-- Table structure for `strategy`
-- ----------------------------
DROP TABLE IF EXISTS `strategy`;
CREATE TABLE `strategy` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '攻略ID',
  `title` varchar(200) NOT NULL COMMENT '攻略标题',
  `user_id` bigint(20) DEFAULT NULL COMMENT '创建用户ID（可为空，系统创建）',
  `category` varchar(50) DEFAULT NULL COMMENT '分类：1day-1日游，2day-2日游，theme-主题游',
  `theme` varchar(100) DEFAULT NULL COMMENT '主题：亲子、情侣、摄影等',
  `cover_image` varchar(255) DEFAULT NULL COMMENT '封面图片',
  `description` text COMMENT '攻略描述',
  `content` longtext COMMENT '详细内容',
  `duration` int(11) DEFAULT NULL COMMENT '游玩时长（天）',
  `budget` decimal(10,2) DEFAULT NULL COMMENT '预算（元）',
  `best_season` varchar(50) DEFAULT NULL COMMENT '最佳季节',
  `tips` text COMMENT '注意事项',
  `view_count` int(11) NOT NULL DEFAULT '0' COMMENT '浏览次数',
  `comment_count` int(11) NOT NULL DEFAULT '0' COMMENT '评论数',
  `favorite_count` int(11) NOT NULL DEFAULT '0' COMMENT '收藏数',
  `like_count` int(11) NOT NULL DEFAULT '0' COMMENT '点赞数',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-草稿，1-发布，2-审核中',
  `is_recommend` tinyint(1) NOT NULL DEFAULT '0' COMMENT '是否推荐：0-否，1-是',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_category` (`category`),
  KEY `idx_status` (`status`),
  KEY `idx_is_recommend` (`is_recommend`),
  FULLTEXT KEY `ft_title_desc` (`title`,`description`),
  CONSTRAINT `fk_strategy_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COMMENT='旅游攻略表';

-- ----------------------------
-- Records of strategy
-- ----------------------------
INSERT INTO `strategy` VALUES ('1', ' 测试', null, '2day', '测试', 'https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg', '111', '', '4', '1111.00', '1', '', '11', '0', '0', '0', '1', '1', '2026-01-05 15:57:29', '2026-01-05 15:57:29');
INSERT INTO `strategy` VALUES ('2', '太原亲子五日游：古建自然美食之旅', null, 'theme', '亲子', 'https://ts1.tc.mm.bing.net/th/id/OIP-C.qOVNXup8kQ0DZg7RTVN3ZwHaE8?w=252&h=211&c=8&rs=1&qlt=90&o=6&dpr=1.3&pid=3.1&rm=2', '这是一份专为亲子家庭设计的太原五日游攻略，预算控制在2000元以内。行程融合了历史古迹、自然风光与地道美食，涵盖晋祠、蒙山大佛、汾河公园等经典景点，穿插动物园、植物园等亲子友好场所。每天行程松紧适度，兼顾文化教育与休闲娱乐，让孩子在游玩中感受三晋文化，品尝刀削面、头脑等特色小吃，入住性价比高的酒店，打造一段温馨有趣的家庭旅行记忆。', '**第一天：初探龙城，感受历史脉搏**\n- **上午**：抵达太原，建议入住**太原桔子酒店(亲贤街店)迎泽店**（约300元/晚，交通便利）。稍作休整后，前往**晋祠博物馆老馆**（门票80元，学生半价）。这里是现存最早的皇家祭祀园林，古木参天，殿宇巍峨，难老泉、圣母殿、宋代彩塑等极具观赏性，适合给孩子讲述历史故事。\n- **下午**：在晋祠附近品尝太原特色面食（如刀削面，人均30元）。随后前往**太原植物园**（门票50元），巨大的木结构温室和丰富的植物种类能让孩子亲近自然，科普性强。\n- **晚上**：前往**钟楼街北区**漫步，感受老商业街氛围，可尝试小吃“老鼠窟元宵”或“认一力”蒸饺（人均40元）。\n\n**第二天：双塔凌霄与城市休闲**\n- **上午**：参观**双塔寺新馆（永祚寺）**（门票30元），登塔远眺太原城景，“双塔凌霄”为太原标志。随后参观旁边的**双塔博物馆**，了解其历史。\n- **下午**：前往**汾河公园北区**（免费），沿汾河骑行或散步，欣赏城市滨水风光，孩子可在宽阔场地玩耍。之后可前往**太原万象城**（免费进入），室内空间大，有亲子娱乐设施和餐饮选择，午餐可在商场解决（人均50元）。\n- **晚上**：品尝太原传统早餐“头脑”（实际可作为晚餐体验，如清和元饭店，人均40元），味道独特，营养丰富。\n\n**第三天：大佛寻踪与古镇探秘**\n- **全天**：前往**蒙山大佛南区**（门票70元，含景区交通）。徒步上山瞻仰世界第二大石刻佛像，沿途山色宜人，路程适中。可带些干粮作为午餐（人均30元）。\n- **下午**：下山后前往**青龙古镇**（免费进入，部分小景点收费）。漫步明清建筑群，体验古商贸镇风貌，有糖画、剪纸等民间工艺可让孩子体验。\n- **晚上**：返回市区，在酒店附近寻找本地菜馆，品尝过油肉、糖醋丸子等晋菜（人均50元）。\n\n**第四天：动物奇趣与滨水时光**\n- **上午**：游览**太原动物园**（门票10元），动物种类较多，园区较大，建议乘坐观光车（另付费）。孩子可与羊驼、小鹿等温和动物互动。\n- **下午**：前往**汾河公园新馆**区域（免费），可选择划船或在水边沙滩玩耍，享受亲子休闲时光。午餐可简单解决（人均30元）。\n- **晚上**：前往食品街（柳巷商圈）寻找美食，如“杨记灌肠”、“雪山冷饮厅”的沙棘冰激凌（人均40元）。\n\n**第五天：文化拾遗与返程**\n- **上午**：根据兴趣选择**晋祠博物馆新馆**（若第一天未尽兴）或**太原矿务局**旧址（外观，免费）感受工业历史。之后整理行李，退房。\n- **下午**：在**中北大学**附近的**摩天轮**（票价约30元）体验，俯瞰汾河景色，为旅程画上轻松句点。午餐可选择本地连锁如“并州大牌档”（人均50元）。随后准备返程。\n\n**预算分配（以一大一小为例，总计约2000元）**：\n- 住宿：300元/晚 × 4晚 = 1200元（选择经济型酒店）\n- 门票：约300元（利用学生优惠）\n- 餐饮：150元/天 × 5天 = 750元\n- 交通及杂费：约250元（公交、出租车及零星消费）\n\n**美食推荐**：除行程提及外，还可尝试“剔尖”、“莜面栲栳栳”、“羊杂割”等。美食街推荐柳巷、食品街。\n\n**住宿建议**：选择**太原桔子酒店(亲贤街店)迎泽店**或**太原桔子酒店(亲贤街店)杏花岭店**，价格适中，设计时尚，位置方便前往各景点。', '5', '2000.00', '全年皆宜，其中4月至10月气候温和，最适合户外游览和亲子活动。', '1. 交通：太原景点较分散，建议结合公交、地铁（已开通1、2号线）和出租车出行；去蒙山大佛、青龙古镇等较远景点可考虑打车或旅游专线。\n2. 门票：部分景点有新旧馆之分，注意区分；许多景点对学生、儿童有优惠，记得携带证件。\n3. 亲子提示：行程中穿插公园、动物园等孩子感兴趣的点，避免全天历史景点；备好零食、水壶和防晒防蚊用品；太原春季风沙较大，秋冬较冷，注意穿衣。\n4. 美食适应：太原面食为主，口味偏咸酸，“头脑”为药膳早餐，可能需适应；可备些肠胃药。\n5. 预算控制：选择经济型酒店，多用公共交通，部分公园免费，门票优先选择核心景点。\n6. 最佳时间：全年皆宜，但4-10月气候更舒适，户外活动体验更佳；冬季可体验冰雪项目，但需注意保暖。', '12', '0', '0', '0', '1', '0', '2026-01-23 16:58:48', '2026-01-23 16:58:48');
INSERT INTO `strategy` VALUES ('3', '测试', '6', '2day', '测试', '测试', '测试', '测试', '1', '2.00', '测试', null, '0', '0', '0', '0', '2', '0', '2026-01-26 13:54:01', '2026-01-26 13:54:01');
INSERT INTO `strategy` VALUES ('4', '测试', '6', '1day', '', '', '测试', '', '1', '0.00', '', null, '0', '0', '0', '0', '2', '0', '2026-01-26 13:54:16', '2026-01-26 13:54:16');
INSERT INTO `strategy` VALUES ('5', 'http://localhost:8081/admin/audit', '6', '1day', '', '', 'http://localhost:8081/admin/audit', '', '1', '0.00', '', null, '0', '0', '0', '0', '2', '0', '2026-01-26 13:56:54', '2026-01-26 13:56:54');
INSERT INTO `strategy` VALUES ('6', '提交失败：Cannot read properties of undefined (reading \'indexOf\')', '6', '1day', null, null, '提交失败：Cannot read properties of undefined (reading \'indexOf\')', null, '1', '0.00', null, null, '0', '0', '0', '0', '2', '0', '2026-01-26 14:02:05', '2026-01-26 14:02:05');
INSERT INTO `strategy` VALUES ('7', '12', '6', '1day', null, null, '12', null, '1', '0.00', null, null, '0', '0', '0', '0', '2', '0', '2026-01-26 14:07:01', '2026-01-26 14:07:01');
INSERT INTO `strategy` VALUES ('8', '111', '6', '1day', null, null, '1111', null, '1', '5.00', null, null, '0', '0', '0', '0', '2', '0', '2026-01-26 14:12:55', '2026-01-26 14:12:55');
INSERT INTO `strategy` VALUES ('9', '太原300元美食两日游', '3', '2day', '美食', null, '本攻略专为预算有限的吃货设计，以300元为上限，在两天内深度体验太原的地标建筑、历史遗迹与市井美食。行程巧妙串联双塔寺、晋祠博物馆、汾河公园等免费或低价景点，重点挖掘街头巷尾的烧烤等本土风味，兼顾文化体验与味蕾享受，用最经济的成本玩转太原精华。', '**第一天：古建探秘与夜市寻味**\n\n*   **上午（预算约10元）：** 前往【双塔寺新馆】（永祚寺）。作为太原的地标，双塔凌霄的景观不容错过。建议早起参观，门票约10元（具体以实际为准，部分时段或免费开放），在此感受古刹的宁静与建筑之美，是开启旅程的绝佳方式。\n*   **中午（预算约30元）：** 在双塔寺附近或前往【钟楼街北区】寻找午餐。钟楼街是太原历史悠久的商业街，周边隐藏着许多地道小吃店。可以尝试一碗地道的太原打卤面或刀削面，人均约15-20元。剩余预算可用于购买一些本地特色小食，如太谷饼。\n*   **下午（预算约0元）：** 漫步至【汾河公园】（北区或新馆均可）。这里是免费的城市绿肺，可以沿着汾河岸散步，欣赏城市风光，感受太原的悠闲生活节奏，是消化午餐、放松身心的好去处。\n*   **晚上（预算约60元）：** 晚餐是重头戏！深入太原的市井小巷，寻找地道的【烧烤】摊档或小店。太原的烧烤风味独特，羊肉串、烤腰子、烤馒头片等都是必点。可以放开品尝，人均预算控制在50-60元。餐后可以在钟楼街或汾河公园周边逛逛，感受太原的夜景。\n\n**第二天：园林怀古与古镇风味**\n\n*   **上午（预算约40元）：** 前往【晋祠博物馆】（老馆或南区）。这是中国现存最早的皇家祭祀园林，历史文化价值极高。门票约40元（学生等或有优惠），是本次行程的文化核心投入。在这里花上2-3小时，细细欣赏古代建筑、雕塑和园林艺术。\n*   **中午（预算约30元）：** 从晋祠出来后，可在景区外围或前往【青龙古镇】解决午餐。青龙古镇保存着明清建筑群，古镇内及周边常有售卖当地小吃，如碗托、莜面栲栳栳等，人均约20-30元，既能果腹又能体验风味。\n*   **下午（预算约0元）：** 游览【青龙古镇】（或新馆）。在古镇的青石板路上漫步，参观古老的院落和商铺，感受明清时期商贸重镇的历史气息。此处游览以闲逛和拍照为主，无需额外门票。\n*   **晚上（预算约130元）：** 返回市区，用最后一餐为旅程画上圆满句号。可以选择一家口碑好的本地餐馆，品尝如过油肉、糖醋丸子等晋菜经典，人均约50-60元。剩余预算可用于购买一些特产或零食。根据预算情况，住宿建议选择经济型酒店，如【太原桔子酒店(亲贤街店)迎泽店】或类似价位酒店，提前预订可能找到200元/晚以下的房间（两日总预算300元主要覆盖餐饮与景点门票，住宿费用需额外计划，但本攻略内推荐酒店作为参考）。若预算严格限定在300元内，则需选择更经济的青旅或住宿方式。\n\n**美食重点推荐：**\n除了上述提到的打卤面、刀削面、烧烤、碗托、莜面栲栳栳、过油肉、糖醋丸子外，务必留意街边的“上帝炸鸡”、“王萍面皮”等本地人喜爱的招牌小吃，物美价廉。\n\n**住宿建议（基于推荐列表及性价比考虑）：**\n考虑到美食探索的便利性和预算，建议选择位于市中心或亲贤商圈附近的【太原桔子酒店(亲贤街店)迎泽店】，交通便利，周边餐饮选择丰富。务必提前在线预订以获取最优价格。', '2', '300.00', '春季（4月-5月）和秋季（9月-10月）。此时太原气候温和宜人，晴朗少雨，非常适合户外游览和城市漫步', '1.  **交通：** 太原市内公交网络发达，景点间可多利用公交车或共享单车，经济实惠。地铁也可作为备选。使用地图APP规划路线。\n2.  **预算控制：** 300元预算主要覆盖门票和餐饮。餐饮可多选择小吃、面食，减少大餐次数。部分景点如汾河公园、青龙古镇主体区域免费，是节省开支的关键。\n3.  **美食探索：** 多向本地人打听，学校、市场周边的小店往往味道正宗且价格亲民。注意饮食卫生，选择客流量较大的摊位或店铺。\n4.  **门票信息：** 出行前务必通过官方渠道核实景点最新门票价格及开放时间，学生、老人等记得携带有效证件享受优惠。\n5.  **时间安排：** 两天行程较紧凑，请合理安排每个景点的停留时间，避免过于劳累。\n6.  **住宿提醒：** 若严格遵循300元总预算，需将住宿成本降至极低（如青旅床位），或考虑一日游。本攻略推荐的酒店作为舒适度参考，实际需根据个人预算灵活调整。', '2', '0', '0', '0', '1', '0', '2026-01-27 14:50:16', '2026-01-27 14:50:16');
INSERT INTO `strategy` VALUES ('10', '太原1日游攻略', null, '1day', '文化', '/uploads/images/strategy_1.jpg', '太原1日游攻略，适合文化主题，预计1天，预算约300元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '1', '300.00', '夏季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '1839', '184', '194', '322', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('11', '太原2日游攻略', null, '2day', '休闲', '/uploads/images/strategy_2.jpg', '太原2日游攻略，适合休闲主题，预计2天，预算约600元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '2', '600.00', '秋季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '4956', '50', '183', '199', '1', '0', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('12', '太原古建筑之旅', null, 'theme', '历史', '/uploads/images/strategy_3.jpg', '太原古建筑之旅，适合历史主题，预计2天，预算约500元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '2', '500.00', '春季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '4303', '40', '186', '333', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('13', '太原美食之旅', null, 'theme', '美食', '/uploads/images/strategy_4.jpg', '太原美食之旅，适合美食主题，预计1天，预算约200元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '1', '200.00', '冬季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '787', '110', '226', '119', '1', '0', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('14', '太原亲子游攻略', null, 'theme', '亲子', '/uploads/images/strategy_5.jpg', '太原亲子游攻略，适合亲子主题，预计2天，预算约800元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '2', '800.00', '夏季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '1709', '145', '139', '163', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('15', '太原摄影之旅', null, 'theme', '摄影', '/uploads/images/strategy_6.jpg', '太原摄影之旅，适合摄影主题，预计2天，预算约600元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '2', '600.00', '冬季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '2210', '188', '120', '135', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('16', '太原周末游', null, '2day', '休闲', '/uploads/images/strategy_7.jpg', '太原周末游，适合休闲主题，预计2天，预算约500元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '2', '500.00', '秋季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '1926', '164', '196', '386', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('17', '太原深度游', null, 'theme', '文化', '/uploads/images/strategy_8.jpg', '太原深度游，适合文化主题，预计3天，预算约1000元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '3', '1000.00', '秋季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '4329', '112', '143', '247', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('18', '太原秋季赏红叶', null, 'theme', '自然', '/uploads/images/strategy_9.jpg', '太原秋季赏红叶，适合自然主题，预计1天，预算约300元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '1', '300.00', '冬季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '1362', '192', '161', '226', '1', '0', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('19', '太原博物馆之旅', null, 'theme', '文化', '/uploads/images/strategy_10.jpg', '太原博物馆之旅，适合文化主题，预计1天，预算约200元。', '\n        【行程安排】\n        第一天：游览主要景点，体验当地文化\n        第二天：继续深度游览（如适用）\n\n        【景点推荐】\n        根据主题选择适合的景点进行游览\n\n        【美食推荐】\n        品尝当地特色美食\n\n        【住宿建议】\n        选择交通便利的酒店\n\n        【注意事项】\n        请提前预订门票和酒店，注意天气变化\n        ', '1', '200.00', '夏季', '建议提前规划行程，注意天气变化，携带必要的证件和物品。', '1069', '115', '107', '103', '1', '1', '2026-01-28 15:51:44', '2026-01-28 15:51:44');
INSERT INTO `strategy` VALUES ('20', '太原亲子美食七日游', '3', 'theme', '亲子', null, '这是一份专为亲子家庭设计的太原七日深度游攻略，以美食为主线，串联起太原的历史文化、自然风光与城市魅力。每天安排一个主题区域，确保行程轻松有趣，既能让孩子在游玩中学习知识，又能全家一起品尝山西地道美食，每天不重样。预算虽有限，但通过合理规划，依然能体验精华。', '**第一天：初识太原，古城寻味**\n- **上午**：抵达太原，入住【太原如家酒店】（解放路店，交通便利，经济实惠）。稍作休整。\n- **下午**：游览【太原古县城】，漫步在明代古城中，感受古代市井文化，孩子可以体验传统手工艺。\n- **美食推荐**：午餐在古县城内品尝【碗托】和【太谷饼】。晚餐尝试【刀削面】，面条劲道，是山西面食的经典代表。\n- **预算分配**：住宿约150元/晚，餐饮约50元/人。\n\n**第二天：晋祠怀古，醋香四溢**\n- **上午**：前往【晋祠】，参观中国现存最早的皇家祭祀园林，感受三千年历史，园林景色优美，适合亲子漫步。\n- **下午**：参观【东湖醋园】，了解老陈醋的传统制作工艺，体验“醋”文化，趣味性强。\n- **美食推荐**：午餐在晋祠附近品尝【过油肉】（山西名菜，肉片滑嫩）。晚餐享用【莜面栲栳栳】，形状独特，口感Q弹。\n- **住宿**：继续入住太原如家酒店。\n- **预算分配**：门票约80元/人，餐饮约60元/人。\n\n**第三天：双塔登高，博物院探秘**\n- **上午**：参观【双塔寺】（永祚寺），登塔俯瞰太原全景，双塔是太原的标志。随后参观旁边的【双塔博物馆】。\n- **下午**：前往【山西博物院】（国家一级博物馆），通过丰富的文物了解山西历史文化，互动展览适合孩子学习。\n- **美食推荐**：午餐尝试【猫耳朵】。晚餐品尝【糖醋里脊】，酸甜可口，孩子喜欢。\n- **住宿**：换至【太原汉庭酒店】（平阳路店），体验不同区域。\n- **预算分配**：交通约20元，餐饮约70元/人。\n\n**第四天：石窟艺术，汾河漫步**\n- **上午**：游览【天龙山石窟】，欣赏北朝至唐代的精美佛教造像，山间空气清新。\n- **下午**：前往【汾河公园】，沿河休闲散步，骑自行车，享受城市生态风光，亲子活动轻松。\n- **美食推荐**：午餐在山下品尝【平遥牛肉】（肉质鲜嫩）。晚餐体验【羊杂割】，汤鲜味美，是特色小吃。\n- **住宿**：继续入住太原汉庭酒店。\n- **预算分配**：交通约30元，餐饮约80元/人。\n\n**第五天：山间乐趣，植物园奇观**\n- **上午**：攀登【太山】，游览古寺，欣赏自然风光，运动量适中。\n- **下午**：参观【太原植物园】，现代化温室展示各种植物花卉，孩子可以学习自然知识。\n- **美食推荐**：午餐在太山附近尝试【灌肠】。晚餐享用【拨鱼儿】，形状可爱，口感爽滑。\n- **住宿**：换至【太原7天酒店】（漪汾街店），靠近汾河。\n- **预算分配**：门票约50元/人，餐饮约60元/人。\n\n**第六天：古镇风情，都市休闲**\n- **上午**：游览【青龙古镇】，探索明清古建筑群，体验古镇生活。\n- **下午**：前往【迎泽公园】，太原市中心最大公园，划船或参观古建筑，放松身心。晚上可去【太原万象城】购物休闲。\n- **美食推荐**：午餐在古镇品尝【油糕】。晚餐尝试【烧烤】，选择多样，满足口味。\n- **住宿**：继续入住太原7天酒店。\n- **预算分配**：交通约40元，餐饮约90元/人。\n\n**第七天：佛光普照，欢乐收官**\n- **上午**：参观【蒙山大佛】，瞻仰世界第二大石佛，景区环境清幽，适合亲子徒步。\n- **下午**：前往【摩天轮】（中北大学附近），乘坐摩天轮俯瞰城市，为孩子带来欢乐体验。之后整理行李，准备返程。\n- **美食推荐**：午餐品尝【头脑】（太原特色早餐，营养丰富，可作早午餐）。晚餐作为收官，再次享用【刀削面】或选择其他未尝试的小吃。\n- **预算分配**：门票约70元/人，餐饮约50元/人。\n\n**全程预算总计**：约10元预算为笔误，实际按经济型规划，每日人均消费约200-300元（含住宿、餐饮、门票、交通），7天总计约1400-2100元，适合家庭控制开支。重点体验美食与核心景点，选择性参观免费或低价景点（如汾河公园、迎泽公园）。', '7', '10.00', '四季皆可，尤以春季（4-5月）和秋季（9-10月）为佳，气候温和，景色宜人，适合户外亲子活动。', '1. **交通建议**：太原景点较分散，建议使用打车软件或公共交通（地铁、公交）结合，经济实惠。亲子游可优先选择出租车，节省体力。\n2. **美食注意**：山西菜偏咸酸，孩子若不适应，点餐时可要求少盐。品尝小吃时注意卫生，选择口碑好的老店。\n3. **亲子安全**：游览石窟、登山时看好孩子，避免攀爬危险区域。博物馆和植物园室内外温差大，注意增减衣物。\n4. **门票优惠**：部分景点（如山西博物院）免费，但需提前预约。学生、儿童、老人常有优惠，带好证件。\n5. **季节调整**：四季皆可，但春季（4-5月）和秋季（9-10月）气候最宜人。夏季注意防晒，冬季保暖，部分山区景点（如崛围山）秋季红叶最美，可酌情调整行程。\n6. **住宿提示**：经济型酒店需提前预订，确保干净安全。选择靠近地铁或公交站的酒店，方便出行。', '2', '0', '0', '0', '1', '0', '2026-03-18 20:34:59', '2026-03-18 20:34:59');
INSERT INTO `strategy` VALUES ('21', '太原7日美食文化深度游', '3', 'theme', '美食', null, '本攻略专为美食爱好者设计，以7天时间深度探索太原的历史文化与地道风味。行程巧妙融合晋祠、山西博物院等文化地标与街头巷尾的特色小吃，确保每日至少品尝一种本地美食。在7元预算框架下，重点推荐高性价比的街头美食体验，兼顾免费景点与低成本交通，带你用味蕾感受山西的醇厚滋味。', '**第一天：初探龙城，品味经典**\n上午抵达太原，入住太原如家酒店（解放路店，交通便利）。下午游览【迎泽公园】（免费），感受城市绿肺的宁静。晚餐品尝【刀削面】（约15元），面条劲道，卤汁香浓。晚上散步【汾河公园】（免费），欣赏城市夜景。\n\n**第二天：晋祠寻古，面食盛宴**\n上午前往【晋祠】（门票旺季80元，淡季65元），欣赏宋代彩塑与千年周柏。午餐在晋祠附近尝试【莜面栲栳栳】（约20元），搭配老陈醋风味独特。下午参观【太原古县城】（免费入城，部分景点收费），体验明代市井文化。晚餐享用【过油肉】（约30元），配一碗米饭。\n\n**第三天：博物知味，醋香四溢**\n上午参观【山西博物院】（免费需预约），全面了解三晋历史。午餐品尝【碗托】（约10元），爽滑酸辣。下午前往【东湖醋园】（门票约20元），了解老陈醋酿造工艺并免费品尝。晚餐尝试【猫耳朵】（约15元），面片小巧筋道。\n\n**第四天：双塔凌空，早餐特色**\n早起体验【头脑】（约25元，太原特色早餐，需适应口味）。上午游览【双塔寺】（门票30元），登塔俯瞰太原全景。午餐简单食用【太谷饼】（约5元/个）。下午参观【双塔博物馆】（免费）。晚餐享用【糖醋里脊】（约28元），酸甜开胃。\n\n**第五天：石窟艺术，夜市寻香**\n上午前往【天龙山石窟】（门票50元），欣赏佛教石刻艺术。午餐自带干粮。下午游览【太山】（门票25元），登山赏景。晚上前往食品街夜市，品尝【灌肠】（约8元）和【烧烤】（人均约30元）。\n\n**第六天：古镇休闲，牛肉飘香**\n上午游览【青龙古镇】（免费，部分宅院收费），感受明清建筑风貌。午餐品尝【平遥牛肉】（约40元/小份），肉质酥烂。下午参观【中国煤炭博物馆】（门票60元，体验井下探秘）。晚餐简单食用【油糕】（约3元/个）。\n\n**第七天：植物园漫步，离别美味**\n上午参观【太原植物园】（门票50元），欣赏现代温室景观。午餐尝试【拨鱼儿】（约18元），形似小鱼趣味十足。下午根据时间自由活动，可选购老陈醋作为伴手礼。晚餐最后品尝【羊杂割】（约20元），汤浓味鲜，结束旅程。\n\n**预算分配说明**：总预算7元为明显笔误，实际日均餐饮预算约60-80元，7日总预算约500元（仅餐饮）。景点门票总计约300元（选择性参观），住宿选择经济型酒店约200元/晚，7日约1400元。实际总预算建议准备2500-3000元。本攻略以美食体验为核心，标注了推荐菜品大致价格，可根据实际调整。', '7', '7.00', '全年皆宜，其中4-5月春季花开、9-10月秋高气爽为最佳，7-8月凉爽宜人但偶有降雨，冬季可体验北方', '1. 美食提示：太原饮食偏酸咸，老陈醋普遍使用；头脑为清晨特供，味道独特建议小份尝试；夜市食品注意卫生。\n2. 交通建议：太原景点较分散，建议使用地铁+公交组合，远程景点可拼车；共享单车适合老城区短途。\n3. 门票优惠：山西博物院、汾河公园等免费景点需提前预约；学生、老人等记得携带证件享受优惠。\n4. 季节适应：太原四季分明，春季风沙较大需戴口罩，夏季凉爽但温差大，秋季最美但需备外套，冬季寒冷需羽绒服。\n5. 饮食安排：每日美食可分散在正餐与小吃中，避免连续重口味；部分小吃摊位只收现金需准备零钱。\n6. 文化尊重：参观寺庙、道观时保持安静，不随意触摸文物；晋祠等古迹禁止吸烟。', '1', '0', '0', '0', '1', '0', '2026-03-18 20:37:03', '2026-03-18 20:37:03');

-- ----------------------------
-- Table structure for `strategy_image`
-- ----------------------------
DROP TABLE IF EXISTS `strategy_image`;
CREATE TABLE `strategy_image` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '图片ID',
  `strategy_id` bigint(20) NOT NULL COMMENT '攻略ID',
  `image_url` varchar(500) NOT NULL COMMENT '图片URL',
  `image_type` varchar(20) DEFAULT 'detail' COMMENT '图片类型：cover-封面，detail-详情',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序顺序',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_sort_order` (`sort_order`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COMMENT='攻略图片表';

-- ----------------------------
-- Records of strategy_image
-- ----------------------------
INSERT INTO `strategy_image` VALUES ('1', '1', 'https://images.pexels.com/photos/1010657/pexels-photo-1010657.jpeg', 'cover', '1', '2026-01-05 15:57:29');

-- ----------------------------
-- Table structure for `strategy_route`
-- ----------------------------
DROP TABLE IF EXISTS `strategy_route`;
CREATE TABLE `strategy_route` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '路线ID',
  `strategy_id` bigint(20) NOT NULL COMMENT '攻略ID',
  `day_number` int(11) NOT NULL COMMENT '第几天',
  `route_order` int(11) NOT NULL COMMENT '路线顺序',
  `attraction_id` bigint(20) DEFAULT NULL COMMENT '景点ID',
  `food_id` bigint(20) DEFAULT NULL COMMENT '美食ID',
  `name` varchar(100) NOT NULL COMMENT '地点名称',
  `description` text COMMENT '描述',
  `start_time` varchar(20) DEFAULT NULL COMMENT '开始时间',
  `end_time` varchar(20) DEFAULT NULL COMMENT '结束时间',
  `cost` decimal(10,2) DEFAULT NULL COMMENT '费用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_id` (`strategy_id`),
  KEY `idx_day_number` (`day_number`),
  CONSTRAINT `fk_strategy_route` FOREIGN KEY (`strategy_id`) REFERENCES `strategy` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='攻略路线表';

-- ----------------------------
-- Records of strategy_route
-- ----------------------------

-- ----------------------------
-- Table structure for `system_config`
-- ----------------------------
DROP TABLE IF EXISTS `system_config`;
CREATE TABLE `system_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '配置ID',
  `config_key` varchar(100) NOT NULL COMMENT '配置键，唯一',
  `config_value` text COMMENT '配置值',
  `config_type` varchar(20) DEFAULT 'string' COMMENT '配置类型：string-字符串，number-数字，json-JSON',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- ----------------------------
-- Records of system_config
-- ----------------------------
INSERT INTO `system_config` VALUES ('1', 'site_name', '家乡文旅宣传平台', 'string', '网站名称', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `system_config` VALUES ('2', 'site_logo', '', 'string', '网站Logo', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `system_config` VALUES ('3', 'contact_phone', '400-000-0000', 'string', '联系电话', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `system_config` VALUES ('4', 'contact_email', 'info@example.com', 'string', '联系邮箱', '2026-01-04 10:08:58', '2026-01-04 10:08:58');
INSERT INTO `system_config` VALUES ('5', 'map_api_key', '', 'string', '地图API密钥', '2026-01-04 10:08:58', '2026-01-04 10:08:58');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `password` varchar(255) NOT NULL COMMENT '密码（BCrypt加密）',
  `nickname` varchar(50) DEFAULT NULL COMMENT '昵称',
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `phone` varchar(20) DEFAULT NULL COMMENT '手机号',
  `avatar` varchar(255) DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint(1) DEFAULT '0' COMMENT '性别：0-未知，1-男，2-女',
  `birthday` date DEFAULT NULL COMMENT '生日',
  `role` varchar(20) NOT NULL DEFAULT 'user' COMMENT '角色：user-普通用户，admin-管理员',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用，1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_username` (`username`),
  KEY `idx_phone` (`phone`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('3', 'admin113', '$2a$10$fra8xPXl9cdKYtPKeoATE.fY8VQ471Ogo7haIK.3oVPZWgya5HZaa', 'admin11', '16025@qq.com', '19722752347', '/uploads/images/e21c583f-0b4a-436f-b51f-a4372956529b.png', '0', null, 'admin', '1', '2026-01-04 15:06:15', '2026-01-04 16:55:23');
INSERT INTO `user` VALUES ('6', '测试用户', '$2a$10$3HUFB03Vlm8n/BAunuTTTuQWMVDVfYYFG9KMQxgNnaGQLDy5lFzMi', '测试用户', '15333646845@163.com', '15333646845', null, '0', null, 'user', '1', '2026-01-26 13:45:17', '2026-01-26 13:45:17');
INSERT INTO `user` VALUES ('7', 'test', '$2a$10$p1GfZpUkeRrOgvAoHwnuj.LIetP6jXPAbA.sjdstKmnKPoqFc.LLy', 'test', '1602510293@qq.com', '19722752347', null, '0', null, 'admin', '1', '2026-03-07 15:13:28', '2026-03-07 15:13:28');
