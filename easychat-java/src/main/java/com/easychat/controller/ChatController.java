package com.easychat.controller;

import com.easychat.annotation.GlobalInterceptor;
import com.easychat.entity.config.AppConfig;
import com.easychat.entity.constants.Constants;
import com.easychat.entity.dto.MessageSendDto;
import com.easychat.entity.dto.TokenUserInfoDto;
import com.easychat.entity.po.ChatMessage;
import com.easychat.entity.vo.ResponseVO;
import com.easychat.enums.ResponseCodeEnum;
import com.easychat.exception.BusinessException;
import com.easychat.services.ChatMessageService;
import com.easychat.services.ChatSessionUserService;
import com.easychat.utils.StringUtils;
import com.sun.xml.internal.bind.v2.TODO;
import org.apache.tomcat.util.bcel.classfile.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

/**
 * @ClassName ChatController
 * @Description 聊天相关接口
 * @Author
 * @Date
 */
@RestController("chatController")
@RequestMapping("/chat")
public class ChatController extends ABaseController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @Resource
    private ChatMessageService chatMessageService;

    @Resource
    private ChatSessionUserService chatSessionUserService;

    @Resource
    private AppConfig appConfig;

    @RequestMapping("/send_message")
    @GlobalInterceptor
    private ResponseVO sendMessage(HttpServletRequest request,
                                   @NotEmpty String contactId,
                                   @NotEmpty @Max(1000) String messageContent,
                                   @NotNull Integer messageType,
                                   Long fileSize,
                                   String fileName,
                                   Integer fileType) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setContactId(contactId);
        chatMessage.setMessageContent(messageContent);
        chatMessage.setMessageType(messageType);
        chatMessage.setFileSize(fileSize);
        chatMessage.setFileName(fileName);
        chatMessage.setFileType(fileType);
        MessageSendDto messageSendDto = chatMessageService.saveMessage(chatMessage, tokenUserInfoDto);
        return getSuccessResponseVO(messageSendDto);
    }

    @RequestMapping("/upload_file")
    @GlobalInterceptor
    private ResponseVO uploadFile(HttpServletRequest request,
                                  @NotNull Long messageId,
                                  @NotNull MultipartFile file,
                                  @NotNull MultipartFile cover) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);
        chatMessageService.saveMessageFile(tokenUserInfoDto.getUserId(), messageId, file, cover);
        return getSuccessResponseVO();
    }

    @RequestMapping("/download_file")
    @GlobalInterceptor
    private void downloadFile(HttpServletRequest request, HttpServletResponse response,
                                  @NotEmpty String fileId, @NotNull Boolean showCover) throws BusinessException {
        TokenUserInfoDto tokenUserInfoDto = getTokenUserInfo(request);

        OutputStream outputStream =null;
        FileInputStream in=null;
        try {
            File file = null;
            if (StringUtils.isNotNumber(fileId)){
                String avatarFolderName = Constants.FILE_FOLDER_FILE+Constants.FILE_FOLDER_FILE_AVATAR_NAME;
                String avatarPath = appConfig.getProjectFolder()+avatarFolderName+fileId+Constants.IMAGE_SUFFIX;
                if (showCover){
                    avatarPath = appConfig.getProjectFolder()+avatarFolderName+fileId+Constants.COVER_IMAGE_SUFFIX;
                }
                file = new File(avatarPath);
                if (!file.exists()){
                    throw new BusinessException(ResponseCodeEnum.CODE_602);
                }
            }else{
                file = chatMessageService.downloadFile(tokenUserInfoDto, Long.parseLong(fileId), showCover);
            }
            response.setContentType("application/x-msdownload;charset=UTF-8");
            response.setHeader("Content-Dispotion", "attachment;");
            response.setContentLengthLong(file.length());

            // 读取文件数据
            in = new FileInputStream(file);
            byte[] byteData = new byte[1024];
            outputStream=response.getOutputStream();
            int len;
            while ((len=in.read(byteData)) != -1){
                outputStream.write(byteData, 0, len);
            }
            outputStream.flush();
        }catch (Exception e){
            logger.error("文件下载失败！！", e);
        }finally {
            if (outputStream!=null){
                try {
                    outputStream.close();
                }catch (Exception e){
                    logger.error("IO异常", e);
                }
            }
            if (in!=null){
                try {
                    in.close();
                }catch (Exception e){
                    logger.error("IO异常", e);
                }
            }
        }
    }
}
