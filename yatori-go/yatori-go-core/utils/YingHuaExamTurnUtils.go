package utils

import (
	"fmt"
	"regexp"
	"strings"
)

// ExamTopics holds a map of ExamTopic indexed by answerId
type ExamTopics struct {
	ExamTopics map[string]ExamTopic
}

// ExamTopic represents a single exam question
type ExamTopic struct {
	AnswerId string        `json:"answerId"`
	Index    string        `json:"index"`
	Source   string        `json:"source"`
	Content  string        `json:"content"`
	Type     string        `json:"type"`
	Selects  []TopicSelect `json:"selects"`
}

// TopicSelect represents a possible answer choice
type TopicSelect struct {
	Value string `json:"value"`
	Num   string `json:"num"`
	Text  string `json:"text"`
}

// 题目转换
func TurnExamTopic(examHtml string) ExamTopics {
	exchangeTopics := ExamTopics{
		ExamTopics: make(map[string]ExamTopic),
	}

	// Regular expression to extract the topic index and answerId
	topicPattern := `<li>[ \f\n\r\t\v]*<a data-id="([^\"]*)"[ \f\n\r\t\v]*href="[^\"]*"[ \f\n\r\t\v]*class="[^\"]*"[ \f\n\r\t\v]*id="[^\"]*"[ \f\n\r\t\v]*data-index="[^\"]*"[ \f\n\r\t\v]*onclick="[^\"]*">([^<]*)</a>[ \f\n\r\t\v]*</li>`
	topicRegexp := regexp.MustCompile(topicPattern)
	topicMap := make(map[string]string)

	// Extract answerId and index
	matches := topicRegexp.FindAllStringSubmatch(examHtml, -1)
	for _, match := range matches {
		answerId := match[1]
		index := match[2]
		topicMap[index] = answerId
	}

	// Regular expression to extract the form containing the exam questions
	formPattern := `<form method="post" action="/api/[^/]*\/submit">([\\w\\W]*?)</form>`
	formRegexp := regexp.MustCompile(formPattern)

	// Extract the form contents
	formMatches := formRegexp.FindAllStringSubmatch(examHtml, -1)
	for _, formMatch := range formMatches {
		topicHtml := formMatch[1]

		// Extracting topic number, type, and source
		topicNumPattern := `<span class="num">[\\D]*?([\\d]+)`
		topicNumRegexp := regexp.MustCompile(topicNumPattern)
		topicNumMatcher := topicNumRegexp.FindStringSubmatch(topicHtml)

		var num, tag, source, content string
		var selects []TopicSelect

		if len(topicNumMatcher) > 0 {
			num = topicNumMatcher[1]
		}

		tagPattern := `<span class="tag">([\\s\\S]*?)</span>`
		tagRegexp := regexp.MustCompile(tagPattern)
		tagMatcher := tagRegexp.FindStringSubmatch(topicHtml)
		if len(tagMatcher) > 0 {
			tag = tagMatcher[1]
		}

		sourcePattern := `<span[ \f\n\r\t\v]*class="txt">[(]*[<span>]*([\\d]*)[</span>]*分[)]*</span>`
		sourceRegexp := regexp.MustCompile(sourcePattern)
		sourceMatcher := sourceRegexp.FindStringSubmatch(topicHtml)
		if len(sourceMatcher) > 0 {
			source = sourceMatcher[1]
		}

		// Extract the question content based on the type of the question (Single choice, Multiple choice, Judgment)
		if tag == "单选" || tag == "多选" || tag == "判断" {
			contentPattern := `<div[ \f\n\r\t\v]*class="content"[ \f\n\r\t\v]*style="[^\"]*">([\\s\\S]*?)</div>`
			contentRegexp := regexp.MustCompile(contentPattern)
			contentMatcher := contentRegexp.FindStringSubmatch(topicHtml)
			if len(contentMatcher) > 0 {
				content = contentMatcher[1]
			}

			// Extract possible selections for the topic
			selectPattern := `<li>[^<]*<label>[^<]*<input type="([^\"]*)"[^\v]*value="([^\"]*)"[ \f\n\r\t\v]*[checked="checked"]*[ \f\n\r\t\v]*class="[^\"]*"[ \f\n\r\t\v]*name="[^\"]*">[ \f\n\r\t\v]*<span class="num">([^<]*)</span>[ \f\n\r\t\v]*<span[ \f\n\r\t\v]*class="txt">([^<]*)</span>[ \f\n\r\t\v]*</label>[ \f\n\r\t\v]*</li>`
			selectRegexp := regexp.MustCompile(selectPattern)
			selectMatches := selectRegexp.FindAllStringSubmatch(topicHtml, -1)
			for _, selectMatch := range selectMatches {
				selectValue := selectMatch[2]
				selectNum := selectMatch[3]
				selectText := selectMatch[4]
				selects = append(selects, TopicSelect{
					Value: selectValue,
					Num:   selectNum,
					Text:  selectText,
				})
			}
			// Clean up content (strip illegal strings)
			content = strings.ReplaceAll(content, "<p>", "")
			content = strings.ReplaceAll(content, "</p>", "\n")
			content = strings.ReplaceAll(content, "&nbsp;", "")
		}

		// Handle Fill-in-the-blank questions
		if tag == "填空" {
			contentPattern := `<div[ \f\n\r\t\v]*class="content"[ \f\n\r\t\v]*style="[^\"]*">([\\s\\S]*?)</div>`
			contentRegexp := regexp.MustCompile(contentPattern)
			contentMatcher := contentRegexp.FindStringSubmatch(topicHtml)
			if len(contentMatcher) > 0 {
				content = contentMatcher[1]
			}

			// Regular expression to extract fill-in-the-blank fields
			fillPattern := `<input ((?<!answer).)+answer_(\\d)+((?<!>).)+>`
			fillRegexp := regexp.MustCompile(fillPattern)
			fillMatches := fillRegexp.FindAllStringSubmatch(topicHtml, -1)
			for _, fillMatch := range fillMatches {
				answerId := fillMatch[2]
				selects = append(selects, TopicSelect{
					Value: answerId,
					Num:   answerId,
					Text:  "",
				})
			}

			// Replace fill-in-the-blank code
			codePattern := `<code>((?<!answer).)+answer_(\\d)+((?<!</code>).)+</code>`
			codeRegexp := regexp.MustCompile(codePattern)
			codeMatches := codeRegexp.FindAllStringSubmatch(content, -1)
			for _, codeMatch := range codeMatches {
				answerId := codeMatch[2]
				content = strings.ReplaceAll(content, codeMatch[0], fmt.Sprintf("（answer_%s）", answerId))
			}

			// Clean up content
			content = strings.ReplaceAll(content, "<p>", "")
			content = strings.ReplaceAll(content, "</p>", "\n")
			content = strings.ReplaceAll(content, "&nbsp;", "")
		}

		// Construct the ExamTopic
		examTopic := ExamTopic{
			AnswerId: topicMap[num],
			Index:    num,
			Source:   source,
			Content:  content,
			Type:     tag,
			Selects:  selects,
		}

		// Add the topic to the ExamTopics map
		exchangeTopics.ExamTopics[topicMap[num]] = examTopic
	}

	return exchangeTopics
}
