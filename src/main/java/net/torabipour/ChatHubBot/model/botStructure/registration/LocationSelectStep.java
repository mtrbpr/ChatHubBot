/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.torabipour.ChatHubBot.model.botStructure.registration;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import java.util.List;
import net.torabipour.ChatHubBot.model.User;
import net.torabipour.ChatHubBot.model.UserStatus;
import net.torabipour.ChatHubBot.model.botStructure.AbstractRegistrationStep;
import net.torabipour.ChatHubBot.model.utils.MediaManager;
import net.torabipour.ChatHubBot.model.utils.UserInterfaceException;
import net.torabipour.ChatHubBot.model.utils.location.NominatimReverseGeocodingJAPI;

/**
 *
 * @author mohammad
 */
public class LocationSelectStep extends AbstractRegistrationStep {

    public LocationSelectStep(Update update, TelegramBot bot) {
        super(update, bot);
    }

    @Override
    protected List<String> getAbortPhrases() {
        List<String> aborts = super.getAbortPhrases();
        aborts.add("Nevermind");
        aborts.add("بی خیال");
        return aborts;
    }

    @Override
    protected UserStatus getAbortUserStatus() {
        return UserStatus.Registered;
    }

    @Override
    protected UserStatus getNextUserStatus() {
        return UserStatus.AgeSelect;
    }

    @Override
    protected void sendMessageOnAbort(Long chatId, Boolean isEnglish, MediaManager mediaManager) {
        sendRegistrationSuccessfull(chatId, isEnglish);
        sendMainMenu(chatId, isEnglish);
    }

    @Override
    protected void onOperation(User localUser, Message message, String messageText) throws UserInterfaceException {
        localUser.setLocationAndAddress(message.location(), new NominatimReverseGeocodingJAPI());
        saveLocalUser();
    }

    @Override
    protected void onAbort(User localUser, Message message, String messageText) {
    }

    @Override
    protected void sendMessageOnSuccess(Long chatId, Boolean isEnglish, MediaManager mediaManager) {
        mediaManager.messageSendKeyboard(isEnglish ? "Send your age." : "سن خود را وارد کنید.", chatId, "/cancel");
    }

    @Override
    protected void validateInput(Message message, String messageText) throws UserInterfaceException {
        if (message.location() == null || message.location().latitude() == null || message.location().longitude() == null) {
            throw new UserInterfaceException("لطفا یک لوکیشن معتبر بفرستید.", "Invalid input for location.");
        }
    }

    @Override
    protected void onInvalidInput(User localUser, Message message, String messageText) {
        mediaManager.locationRequestSend(isEnglish ? "Send your current location." : "موقعیت جغرافیایی خود را ارسال نمایید.",
                chatId, isEnglish ? "Send Location" : "ارسال موقعیت", isEnglish);
    }

    @Override
    protected void onUnsuccessfullOperation(User localUser, Message message, String messageText) {
        onInvalidInput(localUser, message, messageText);
    }

}
