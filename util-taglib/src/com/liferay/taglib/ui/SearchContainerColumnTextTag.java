/**
 * Copyright (c) 2000-2008 Liferay, Inc. All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.liferay.taglib.ui;

import com.liferay.portal.kernel.bean.BeanPropertiesUtil;
import com.liferay.portal.kernel.dao.search.ResultRow;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.dao.search.SearchEntry;
import com.liferay.portal.kernel.dao.search.TextSearchEntry;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.taglib.util.ParamAndPropertyAncestorTagImpl;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletURL;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyContent;

/**
 * <a href="SearchContainerColumnTextTag.java.html"><b><i>View Source</i></b>
 * </a>
 *
 * @author Raymond Augé
 *
 */
public class SearchContainerColumnTextTag
	extends ParamAndPropertyAncestorTagImpl {

	public int doAfterBody() {
		return SKIP_BODY;
	}

	public int doEndTag() {
		try {
			SearchContainerRowTag parentTag =
				(SearchContainerRowTag)findAncestorWithClass(
					this, SearchContainerRowTag.class);

			ResultRow row = parentTag.getRow();

			if (Validator.isNotNull(_property)) {
				_value = String.valueOf(
					BeanPropertiesUtil.getObject(row.getObject(), _property));
			}
			else {
				if (Validator.isNotNull(_buffer)) {
					_value = _sb.toString();
				}
				else if (Validator.isNull(_value)) {
					BodyContent bc = getBodyContent();

					if (bc != null) {
						_value = bc.getString();
					}
				}
			}

			if (_index <= -1) {
				_index = row.getEntries().size();
			}

			if (row.isRestricted()) {
				_href = null;
			}

			row.addText(
				_index,
				new TextSearchEntry(
					getAlign(), getValign(), getColspan(), getValue(),
					(String)getHref(), getTarget(), getTitle()));

			return EVAL_PAGE;
		}
		finally {
			_align = SearchEntry.DEFAULT_ALIGN;
			_buffer = null;
			_colspan = SearchEntry.DEFAULT_COLSPAN;
			_href = null;
			_index = -1;
			_name = null;
			_orderable = false;
			_orderableProperty = null;
			_property = null;
			_target = null;
			_title = null;
			_valign = SearchEntry.DEFAULT_VALIGN;
			_value = null;
		}
	}

	public int doStartTag() throws JspException {
		SearchContainerRowTag parentRowTag = (SearchContainerRowTag)
			findAncestorWithClass(this, SearchContainerRowTag.class);

		if (parentRowTag == null) {
			throw new JspTagException(
				"Requires liferay-ui:search-container-row");
		}

		if (!parentRowTag.isHeaderNamesAssigned()) {
			SearchContainerTag parentSearchContainerTag =
				(SearchContainerTag)findAncestorWithClass(
					this, SearchContainerTag.class);

			SearchContainer searchContainer =
				parentSearchContainerTag.getSearchContainer();

			List<String> headerNames =
				parentSearchContainerTag.getHeaderNames();

			String name = getName();

			if (Validator.isNull(name) && Validator.isNotNull(_property)) {
				name = _property;
			}

			if (headerNames == null) {
				headerNames = new ArrayList<String>();

				parentSearchContainerTag.setHeaderNames(headerNames);
				searchContainer.setHeaderNames(headerNames);
			}

			headerNames.add(name);

			if (_orderable) {
				Map<String,String> orderableHeaders =
					searchContainer.getOrderableHeaders();

				if (orderableHeaders == null) {
					orderableHeaders = new LinkedHashMap<String, String>();

					searchContainer.setOrderableHeaders(orderableHeaders);
				}

				if (Validator.isNotNull(_orderableProperty)) {
					orderableHeaders.put(getName(), _orderableProperty);
				}
				else if (Validator.isNotNull(_property)) {
					orderableHeaders.put(getName(), _property);
				}
			}
		}

		if (Validator.isNotNull(_property)) {
			return SKIP_BODY;
		}
		else if (Validator.isNotNull(_buffer)) {
			_sb = new StringBuilder();

			pageContext.setAttribute(_buffer, _sb);

			return EVAL_BODY_INCLUDE;
		}
		else if (Validator.isNull(_value)) {
			return EVAL_BODY_BUFFERED;
		}
		else {
			return SKIP_BODY;
		}
	}

	public String getAlign() {
		return _align;
	}

	public String getBuffer() {
		return _buffer;
	}

	public int getColspan() {
		return _colspan;
	}

	public Object getHref() {
		if (Validator.isNotNull(_href) && (_href instanceof PortletURL)) {
			_href = _href.toString();
		}

		return _href;
	}

	public int getIndex() {
		return _index;
	}

	public String getName() {
		if (Validator.isNull(_name)) {
			return StringPool.BLANK;
		}
		else {
			return _name;
		}
	}

	public String getOrderableProperty() {
		return _orderableProperty;
	}

	public String getProperty() {
		return _property;
	}

	public String getTarget() {
		return _target;
	}

	public String getTitle() {
		return _title;
	}

	public String getValign() {
		return _valign;
	}

	public String getValue() {
		return _value;
	}

	public boolean isOrderable() {
		return _orderable;
	}

	public void setAlign(String align) {
		_align = align;
	}

	public void setBuffer(String buffer) {
		_buffer = buffer;
	}

	public void setColspan(int colspan) {
		_colspan = colspan;
	}

	public void setHref(Object href) {
		_href = href;
	}

	public void setIndex(int index) {
		_index = index;
	}

	public void setName(String name) {
		_name = name;
	}

	public void setOrderable(boolean orderable) {
		_orderable = orderable;
	}

	public void setOrderableProperty(String orderableProperty) {
		_orderableProperty = orderableProperty;
	}

	public void setProperty(String property) {
		_property = property;
	}

	public void setTarget(String target) {
		_target = target;
	}

	public void setTitle(String title) {
		_title = title;
	}

	public void setValign(String valign) {
		_valign = valign;
	}

	public void setValue(String value) {
		_value = value;
	}

	private String _align = SearchEntry.DEFAULT_ALIGN;
	private String _buffer;
	private int _colspan = SearchEntry.DEFAULT_COLSPAN;
	private Object _href;
	private int _index = -1;
	private String _name;
	private boolean _orderable;
	private String _orderableProperty;
	private String _property;
	private StringBuilder _sb;
	private String _target;
	private String _title;
	private String _valign = SearchEntry.DEFAULT_VALIGN;
	private String _value;

}