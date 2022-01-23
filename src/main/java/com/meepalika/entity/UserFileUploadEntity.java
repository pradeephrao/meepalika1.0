package com.meepalika.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "media")
public class UserFileUploadEntity extends Auditable<Long>{

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;

	@Column(name = "original_filename")
	private String original_filename;

	@Column(name = "file_type")
	private String file_type;

	@Column(name = "media_type")
	private String media_type;

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id", referencedColumnName = "id")
	private User user;

	@Column(name = "file_size")
	private String file_size;

	@Transient
	private String storage_path;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getOriginal_filename() {
		return original_filename;
	}

	public void setOriginal_filename(String original_filename) {
		this.original_filename = original_filename;
	}

	public String getFile_type() {
		return file_type;
	}

	public void setFile_type(String file_type) {
		this.file_type = file_type;
	}

	public String getMedia_type() {
		return media_type;
	}

	public void setMedia_type(String media_type) {
		this.media_type = media_type;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFile_size() {
		return file_size;
	}

	public void setFile_size(String file_size) {
		this.file_size = file_size;
	}

	public String getStorage_path() {
		return storage_path;
	}

	public void setStorage_path(String storage_path) {
		this.storage_path = storage_path;
	}

	@Override
	public String toString() {
		return "UserFileUploadEntity [id=" + id + ", original_filename=" + original_filename + ", file_type="
				+ file_type + ", media_type=" + media_type + ", user=" + user + ", file_size=" + file_size
				+ ", storage_path=" + storage_path + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((file_size == null) ? 0 : file_size.hashCode());
		result = prime * result + ((file_type == null) ? 0 : file_type.hashCode());
		result = prime * result + id;
		result = prime * result + ((media_type == null) ? 0 : media_type.hashCode());
		result = prime * result + ((original_filename == null) ? 0 : original_filename.hashCode());
		result = prime * result + ((storage_path == null) ? 0 : storage_path.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserFileUploadEntity other = (UserFileUploadEntity) obj;

		if (file_size == null) {
			if (other.file_size != null)
				return false;
		} else if (!file_size.equals(other.file_size))
			return false;
		if (file_type == null) {
			if (other.file_type != null)
				return false;
		} else if (!file_type.equals(other.file_type))
			return false;
		if (id != other.id)
			return false;
		if (media_type == null) {
			if (other.media_type != null)
				return false;
		} else if (!media_type.equals(other.media_type))
			return false;
		if (original_filename == null) {
			if (other.original_filename != null)
				return false;
		} else if (!original_filename.equals(other.original_filename))
			return false;
		if (storage_path == null) {
			if (other.storage_path != null)
				return false;
		} else if (!storage_path.equals(other.storage_path))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

}
