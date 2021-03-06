/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.tools.bundle.support.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;

import com.liferay.portal.tools.bundle.support.internal.util.BundleSupportUtil;
import com.liferay.portal.tools.bundle.support.internal.util.FileUtil;

import java.io.File;
import java.io.IOException;

import java.nio.file.Files;

/**
 * @author David Truong
 */
@Parameters(
	commandDescription = "Delete a file from the deploy directory of a Liferay bundle.",
	commandNames = "clean"
)
public class CleanCommand extends BaseCommand {

	@Override
	public void execute() throws IOException {
		String fileName = FileUtil.getFileName(_fileName);

		String extension = FileUtil.getExtension(fileName);

		String deployFolder = BundleSupportUtil.getDeployFolder(extension);

		File file = new File(getLiferayHomeDir(), deployFolder + fileName);

		Files.deleteIfExists(file.toPath());
	}

	public String getFileName() {
		return _fileName;
	}

	public void setFileName(String fileName) {
		_fileName = fileName;
	}

	@Parameter(
		description = "The name of the file to delete from your bundle.",
		names = {"-f", "--file"}, required = true
	)
	private String _fileName;

}