package exercise;

import java.io.File;
import java.io.Serializable;
import java.net.URI;
import java.nio.file.FileSystem;

/**
 * @author Changliang Tao
 *
 */
public class Exercise1 implements Serializable, Comparable<File> {
	/**
	 * 平台本地文件系统的文件系统对象
	 */
	static private FileSystem fs = FileSystem.getFileSystem();
	
	/**
	 * 标准化路径名 
	 */
	private String path;
	/**
	 * 枚举文件路径状态
	 */
	private static enum PathStatus { INVALID, CHECKED };
	/**
	 * 判断文件路径是否有效的标记
	 */
	private transient PathStatus status = null; 
	/**
	 * 只进行Null字符检查 
	 * 如果返回为false,也不能保证是有效的
	 * @return true, 如果文件路径是无效的
	 */
	final boolean isInvalid() {
		if (status == null) {
			status = (this.path.indexOf('\u0000') < 0) ? PathStatus.CHECKED
													   : PathStatus.INVALID;
		}
		return status == PathStatus.INVALID;
	}
	/**
	 * 路径名的前缀长度，没有前缀则为零
	 */
	private transient int prefixLength;
	/**
	 * @return get前缀长度
	 */
	int getPrefixLength() {
		return prefixLength;
	}
	/**
	 * 系统的默认文件分隔符, unix--> '/'; windows--> '\\';
	 */
	public static final char separatorChar = fs.getSeparator();
	/**
	 * 系统的默认文件分隔符
	 */
	public static final String separator = "" + separatorChar;
	/**
	 * 系统的默认路径分隔符，unix--> ':'; windows--> ';'
	 */
	public static final char pathSeparatorChar = fs.getPathSeparator();
	/**
	 * 默认路径分隔符
	 */
	public static final String pathSeparator = "" + pathSeparatorChar;
	
	/* -- 构造函数 -- */
	
	/**
	 * 内部构造器
	 */
	private Exercise1(String pathname, int prefixLength) {
		this.path = path = pathname;
		this.prefixLength = prefixLength;
	}
	/**
	 * 内部构造器
	 */
	private Exercise1(String child, File parent) {
		assert parent.path ！= null;
		assert (!parent.path.equals(""));
		this.path = fs.resolve(parent.path, child);
		this.prefixLength = parent.prefixLength;
	}
	/**
	 * 创建实例
	 * @param pathname 一个路径字符串
	 */
	public Exercise1(String pathname) {
		if (pathname == null) {
			throw new NullPointerException();
		} 
		this.path = fs.normalize(pathname);
		this.prefixLength = fs.prefixLength(this.path);
	}
	/**
	 * 创建实例
	 * @param parent 父级路径名
	 * @param child 子级路径名
	 */
	public Exercise1(String parent, String child) {
		if (child == null) {
			this.path = fs.resolve(fs.getDefaultParent(), fs.normalize(child));
		} else {
			this.path = fs.resolve(fs.normalize(parent), fs.noramlize(child));
		}
		this.prefixLength = fs.prefixLength(this.path);
	}
	/**
	 * 创建实例
	 * @param uri FILE:型的路径名
	 */
	public Exercise1(URI uri) {
		// 检查前提条件
		if (!uri.isAbsolute()) 
			throw new IllegalArgumentException("uri不是绝对路径");
		if (uri.isOpaque())
			throw new IllegalArgumentException("uri不是层级");
		String scheme = uri.getScheme();
		if ((scheme == null) || !scheme.equalsIgnoreCase("file"))
			throw new IllegalArgumentException("uri不是\"file\"协议");
		if (uri.getAuthority() != null) 
			throw new IllegalArgumentException("uri有一个authority组件");
		if (uri.getFragment() != null)
			throw new IllegalArgumentException("uri有一个fragment组件");
		if (uri.getQuery() != null) 
			throw new IllegalArgumentException("uri有一个query组件");
		String p = uri.getPath();
		if (p.equals(""))
			throw new IllegalArgumentException("uri路径组件是空的");
		// 开始初始化
		p = fs.fromURIPath(p);
		if (Exercise1.separatorChar != '/')
			p = p.replace('/', Exercise1.separatorChar);
		this.path = fs.normalize(p);
		this.prefixLength = fs.prefixLength(this.path);
	}
	
}






















