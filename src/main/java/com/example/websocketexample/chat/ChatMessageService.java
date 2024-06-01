package com.example.websocketexample.chat;

import com.example.websocketexample.chatroom.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomService chatRoomService;

    /**
     * Saves a chat message by setting the chat ID and saving it to the repository.
     *
     * @param  chatMessage  the chat message to be saved
     * @return              the saved chat message
     * @throws RuntimeException if the chat room is not found
     */
    public ChatMessage save(ChatMessage chatMessage) {
        var chatId = chatRoomService.getChatRoomId(
                chatMessage.getSenderId(),
                chatMessage.getRecipientId(),
                true)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        chatMessage.setChatId(chatId);
        return chatMessageRepository.save(chatMessage);
    }

    public List<ChatMessage> findChatMessages(
            String senderId,
            String recipientId) {
        var chatId = chatRoomService.getChatRoomId(
                senderId,
                recipientId,
                false);

        return chatId.map(chatMessageRepository::findByChatId).orElse(new ArrayList<>());
    }

}
