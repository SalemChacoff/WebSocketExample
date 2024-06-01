package com.example.websocketexample.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;

    /**
     * Retrieves the chat room ID for the given sender and recipient IDs.
     * If a chat room exists with the given sender and recipient IDs, returns the chat room ID.
     * If no chat room exists with the given sender and recipient IDs and createNewRoomIfNotExists is true,
     * creates a new chat room with the given sender and recipient IDs and returns the chat room ID.
     * If no chat room exists with the given sender and recipient IDs and createNewRoomIfNotExists is false,
     * returns an empty Optional.
     *
     * @param  senderId               the ID of the sender
     * @param  recipientId            the ID of the recipient
     * @param  createNewRoomIfNotExists   indicates whether to create a new chat room if it doesn't exist
     * @return                        an Optional containing the chat room ID, or an empty Optional if no chat room exists
     */
    public Optional<String> getChatRoomId(
            String senderId,
            String recipientId,
            boolean createNewRoomIfNotExists) {
        return chatRoomRepository.findBySenderIdAndRecipientId(senderId, recipientId)
                .map(ChatRoom::getChatId)
                .or(() -> {
                   if (createNewRoomIfNotExists) {
                       var chatId = createChatId(senderId, recipientId);
                       return Optional.of(chatId);
                   }
                   return Optional.empty();
                });
    }


    /**
     * Creates a chat ID by concatenating the sender ID and recipient ID with an underscore.
     * Saves two ChatRoom objects in the chatRoomRepository: one with the sender ID and recipient ID as sender and recipient,
     * and the other with the recipient ID and sender ID as sender and recipient.
     *
     * @param  senderId   the ID of the sender
     * @param  recipientId    the ID of the recipient
     * @return             the created chat ID
     */
    private String createChatId(String senderId, String recipientId) {
        var chatId = String.format(("%s_%s"), senderId, recipientId);
        ChatRoom senderRecipient = ChatRoom.builder()
                .chatId(chatId)
                .senderId(senderId)
                .recipientId(recipientId)
                .build();

        ChatRoom recipientSender = ChatRoom.builder()
                .chatId(chatId)
                .senderId(recipientId)
                .recipientId(senderId)
                .build();

        chatRoomRepository.save(senderRecipient);
        chatRoomRepository.save(recipientSender);

        return chatId;
    }
}
