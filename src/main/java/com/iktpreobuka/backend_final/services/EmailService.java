package com.iktpreobuka.backend_final.services;

import com.iktpreobuka.backend_final.models.EmailObject;

public interface EmailService {

	void sendSimpleMessage(EmailObject object);
	void sendTemplateMessage(EmailObject object) throws Exception;
	void sendMessageWithAttachment(EmailObject object, String pathToAttachment) throws Exception;
}
