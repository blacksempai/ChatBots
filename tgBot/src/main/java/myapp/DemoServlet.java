/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package myapp;

import myapp.bot.Bot;
import myapp.bot.BotWebSender;
import myapp.model.Resume;
import myapp.model.ResumesFromWebsiteHolder;

import java.io.IOException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DemoServlet extends HttpServlet {

  @Override
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
          throws ServletException, IOException {
    RequestDispatcher dispatcher = req.getRequestDispatcher("/");
    dispatcher.forward(req,resp);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    //BotWebSender tgBot = (BotWebSender) getServletContext().getAttribute("bot");
    Resume resume = getResumeFromRequest(req);
    //tgBot.sendResumeToOwner(resume);
    ResumesFromWebsiteHolder.getResumes().add(resume);
    resp.setContentType("text/html");
    RequestDispatcher dispatcher = req.getRequestDispatcher("/success.html");
    dispatcher.forward(req,resp);
  }

  private Resume getResumeFromRequest(HttpServletRequest req){
    Resume resume = new Resume();
    resume.setFullName(req.getParameter("fullName"));
    resume.setAge(req.getParameter("age"));
    resume.setTelephone(req.getParameter("telephone"));
    resume.setSkypeLogin(req.getParameter("skypeLogin"));
    resume.setTgUsername("IP_address "+req.getRemoteAddr());
    return resume;
  }

}
