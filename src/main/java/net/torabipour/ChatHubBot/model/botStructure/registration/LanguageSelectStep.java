/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.torabipour.ChatHubBot.model.botStructure.registration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import net.torabipour.ChatHubBot.db.TransactionalDBAccess;
import net.torabipour.ChatHubBot.model.Language;
import net.torabipour.ChatHubBot.model.User;
import net.torabipour.ChatHubBot.model.UserStatus;
import net.torabipour.ChatHubBot.model.botStructure.AbstractRegistrationStep;
import net.torabipour.ChatHubBot.model.utils.MediaManager;
import net.torabipour.ChatHubBot.model.utils.UserInterfaceException;
import org.hibernate.Session;

/**
 *
 * @author mohammad
 */
public class LanguageSelectStep extends AbstractRegistrationStep {

    public LanguageSelectStep(Update update, TelegramBot bot) {
        super(update, bot);
    }

    @Override
    protected UserStatus getAbortUserStatus() {
        return UserStatus.LanguageSelect;
    }

    @Override
    protected UserStatus getNextUserStatus() {
        return UserStatus.SexSelect;
    }

    @Override
    protected void sendMessageOnAbort(Long chatId, Boolean isEnglish, MediaManager mediaManager) {
        mediaManager.messageSendKeyboard("زبان خود را انتخاب کنید. \n Choose your language.", chatId, "English 🇬🇧", "Persian 🇮🇷");
    }

    @Override
    protected void onOperation(User localUser, Message message, String messageText) throws UserInterfaceException {
        localUser.setLang(Language.valueOf(messageText.split(" ")[0]));
        saveLocalUser();
    }

    @Override
    protected void sendMessageOnSuccess(Long chatId, Boolean isEnglish, MediaManager mediaManager) {
        mediaManager.messageSendKeyboard(isEnglish ? "Select your sex." : "جنسیت خود را انتخاب کنید.", chatId,
                new String[]{isEnglish ? "Male 👨‍🦱" : "مرد 👨‍🦱", isEnglish ? "Female 👩" : "زن 👩"});
    }

    @Override
    protected void validateInput(Message message, String messageText) throws UserInterfaceException {
        try {
            Language.valueOf(messageText.split(" ")[0]);
        } catch (Exception ex) {
            throw new UserInterfaceException("مقدار وارد شده اشتباه است.", "Invalid input for language.");
        }
    }
    
    @Override
    protected void onAbort(User localUser, Message message, String messageText) {
    }

    @Override
    protected void onInvalidInput(User localUser, Message message, String messageText) {
        sendMessageOnAbort(chatId, isEnglish, mediaManager);
    }

    @Override
    protected void onUnsuccessfullOperation(User localUser, Message message, String messageText) {
        sendMessageOnAbort(chatId, isEnglish, mediaManager);
    }

}
